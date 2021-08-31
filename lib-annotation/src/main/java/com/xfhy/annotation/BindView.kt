package com.xfhy.annotation

/**
 * @author : xfhy
 * Create time : 2021/8/31 7:41 上午
 * Description : bind注解
 * 只需要保存到SOURCE源码阶段就行了,因为在APT处理的时候是在编译的时候,编译之后就用不到这个注解了
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class BindView(val value: Int = 0)