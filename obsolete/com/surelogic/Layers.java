/*
 * Copyright (c) 2015 SureLogic, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.surelogic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation for multiple {@link Layer} annotations. It is a modeling
 * error for an entity to have both a {@link Layers} and a {@link Layer}
 * annotation.
 * 
 * <h3>Semantics:</h3>
 * 
 * This annotation holds a list of {@link Layer} declarations without imposing
 * any further constraint on the program's implementation.
 * 
 * <h3>Examples:</h3>
 * 
 * Declaring two type sets on the same package (only in a package-info.java
 * file).
 * 
 * <pre>
 * &#064;Layers({ @Layer(&quot;MODEL may refer to UTIL&quot;), @Layer(&quot;CONTROLLER may refer to MODEL | java.io.File&quot;) })
 * package org.example;
 * 
 * import com.surelogic.*;
 * </pre>
 * 
 * @see Layer
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface Layers {
  /**
   * The {@link Layer} annotations to apply to the package.
   * 
   * @return the {@link Layer} annotations to apply to the package.
   */
  Layer[] value();
}
