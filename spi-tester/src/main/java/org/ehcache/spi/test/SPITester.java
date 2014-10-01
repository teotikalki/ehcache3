/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehcache.spi.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Hung Huynh
 */
public abstract class SPITester {

  public Result runTestSuite() {
    Result result = new Result();
    result.testRunStarted();
    for (Method m : getClass().getDeclaredMethods()) {
      if (m.isAnnotationPresent(SPITest.class)) {
        try {
          m.invoke(this, (Object[]) null);
          result.testFinished();
        } catch (InvocationTargetException wrappedExc) {
          Failure failure = new Failure(m.getName(), wrappedExc.getCause());
          result.testFailure(failure);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
    result.testRunFinished();
    return result;
  }
}