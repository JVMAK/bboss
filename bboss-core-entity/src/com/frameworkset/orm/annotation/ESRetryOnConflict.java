package com.frameworkset.orm.annotation;/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于批量操作
 * In between the get and indexing phases of the update, it is possible that another process might have already updated the same document.
 * By default, the update will fail with a version conflict exception.
 * The retry_on_conflict parameter controls how many times to retry the update before finally throwing an exception.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ESRetryOnConflict {
	/**
	 * 标识是否保存注解对应的字段的值
	 * @return
	 */
	boolean persistent() default false;
}
