/**
 *
 * Copyright (C) 2009 Global Cloud Specialists, Inc. <info@globalcloudspecialists.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jclouds.logging.Logger;

import com.google.common.base.Function;

/**
 * Transforms the result of a future as soon as it is available.
 * 
 * @author Adrian Cole
 */
public class FutureFunctionCallable<F, T> implements Callable<T> {

   private final Future<F> future;
   private final Function<F, T> function;
   private final Logger logger;

   public FutureFunctionCallable(Future<F> future, Function<F, T> function) {
      this(future, function, Logger.NULL);
   }

   public FutureFunctionCallable(Future<F> future, Function<F, T> function, Logger logger) {
      this.future = future;
      this.function = function;
      this.logger = logger;
   }

   public T call() throws Exception {
      try {
         F input = future.get();
         logger.debug("Processing intermediate result for: %s", input);
         T result = function.apply(input);
         logger.debug("Processed intermediate result for: %s", input);
         return result;
      } catch (ExecutionException e) {
         throw (Exception) e.getCause();
      }
   }

}
