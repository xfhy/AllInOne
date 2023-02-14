#include <jni.h>
#include <cstdio>
#include <cstring>
#include <inttypes.h>
#include <elf.h>
#include <unistd.h>
#include <sys/mman.h>
#include "common.h"


#define PAGE_START(addr) ((addr) & PAGE_MASK)
#define PAGE_END(addr)   (PAGE_START(addr) + PAGE_SIZE)
//
// Created by xfhy on 2023/2/10.
//

void pltHook() {

    /*

HWBKL:/ # ps -A |grep xfhy
u0_a184       8644   551 5091940  93852 SyS_epoll_wait      0 S com.xfhy.allinone
HWBKL:/ # cat /proc/8644/maps | grep malloc
7d9f400000-7dbb400000 rw-p 00000000 00:00 0                              [anon:libc_malloc]
7dcf000000-7dcf200000 rw-p 00000000 00:00 0                              [anon:libc_malloc]
7dd0600000-7dd0a00000 rw-p 00000000 00:00 0                              [anon:libc_malloc]
7dd0c00000-7dd0e00000 rw-p 00000000 00:00 0                              [anon:libc_malloc]
7dd2250000-7dd2251000 r-xp 00000000 103:2d 26907                         /data/app/com.xfhy.allinone-TZdKdB8r5fwyjroydOVPYA==/lib/arm64/libtestmalloc.so
7dd2251000-7dd2252000 r--p 00000000 103:2d 26907                         /data/app/com.xfhy.allinone-TZdKdB8r5fwyjroydOVPYA==/lib/arm64/libtestmalloc.so
7de5200000-7de5400000 rw-p 00000000 00:00 0                              [anon:libc_malloc]
7de6c00000-7de6e00000 rw-p 00000000 00:00 0                              [anon:libc_malloc]
7de8600000-7de8800000 rw-p 00000000 00:00 0                              [anon:libc_malloc]
7def000000-7df1800000 rw-p 00000000 00:00 0                              [anon:libc_malloc]

 */

    //--------------------------------1. 找到libtestmalloc.so的地址
    //进程的虚拟内存每一页上存放了什么数据，都会记录在maps文件中
    //看上面的maps文件的部分内容，我们可以找到libtestmalloc.so加载进内存之后的起始和结束地址
    //我们需要一行一行地读取该文件内容，从而找到libtestmalloc.so的基地址（起始地址）
    FILE *fp = fopen("/proc/self/maps", "r");
    char line[200];
    unsigned int base_addr = 0;
    //读取maps文件内容，一行一行地读
    while (fgets(line, sizeof(line), fp)) {
        //读取到有libtestmalloc.so内容就break,同时把地址记录下来
        // “PRIxPTR”是将数据转换成 16 进制地址格式的标志，然后赋值给 base_addr
        if (nullptr != strstr(line, "libtestmalloc.so") &&
            sscanf(line, "%" PRIxPTR"-%*lx %*4s 00000000", &base_addr) == 1) {
            LOGD("%s", line);
            break;
        }
    }
    fclose(fp);

    //---------------------------------2.计算 so 库中程序头部表的地址，头部表中记录了 ELF 文件中各个段的地址：

    //将 base_addr 强制转换成Elf32_Ehdr格式，即32位 ELF header的结构体，如果是 64 位需要转换成 Elf64_Ehdr
    Elf64_Ehdr *header = (Elf64_Ehdr *) (base_addr);
    Elf64_Phdr *phdr_table = (Elf64_Phdr *) (base_addr + header->e_phoff);  // 程序头部表的地址
    if (phdr_table == 0) {
        return;
    }
    size_t phr_count = header->e_phnum;  // 程序头表项个数
    LOGD("程序头表项个数: %d", phr_count);

    //-----------------3.遍历程序头部表，获取 .dynamic 段的地址
    unsigned long dynamicAddr;
    unsigned int dynamicSize;
    for (int i = 0; i < phr_count; i++) {
        if (phdr_table[i].p_type == PT_DYNAMIC) {
            //so基地址加dynamic段的偏移地址，就是dynamic段的实际地址
            dynamicAddr = phdr_table[i].p_vaddr + base_addr;
            dynamicSize = phdr_table[i].p_memsz;
            break;
        }
    }

    //----------------4.遍历 .dynamic 段，d_tag为 3（不同平台下的 so，这里的序列可能会不一样，所以为了兼容性考虑，我们最好用名称来进行确认） 即为 .got.plt 表地址：
    int symbolTableAddr = 0;
    Elf32_Dyn *dynamic_table = (Elf32_Dyn *) dynamicAddr;
    for (int i = 0; i < dynamicSize; i++) {
        int val = dynamic_table[i].d_un.d_val;
        if (dynamic_table[i].d_tag == 3) {
            symbolTableAddr = val + base_addr;
            break;
        }
    }

    //-------------5.修改内存属性为可写，并遍历 .got.plt 表，找到 Malloc 函数的地址后，将 Malloc 函数地址替换成我们自己的 Malloc_hook 函数地址：
    //读写权限改为可写
    mprotect((void *) PAGE_START(symbolTableAddr), PAGE_SIZE, PROT_READ | PROT_WRITE);

//    int oldFunc = &malloc - (int) base_addr; // 目标函数偏移地址
//    int newFunc = &malloc_hook;  // 替换的hook函数的偏移地址
//    int i = 0;
//    while (1) {
//        if (symbolTableAddr[i].st_value == oldFunc) {
//            symbolTableAddr[i].st_value = newFunc;
//            break;
//        }
//        i++;
//    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xfhy_nativelib_MonitorMalloc_startMonitor(JNIEnv *env, jobject thiz) {
    pltHook();
}