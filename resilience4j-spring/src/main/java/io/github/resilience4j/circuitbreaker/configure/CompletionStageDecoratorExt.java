/*
 * Copyright 2019 Mahmoud Romeh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.resilience4j.circuitbreaker.configure;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.CheckedFunction0;

import java.util.concurrent.*;

/**
 * Decorator support for return type which implement CompletionStage
 */
public class CompletionStageDecoratorExt implements CircuitBreakerDecoratorExt {

    /**
     * @param returnType the AOP method return type class
     * @return boolean if the return type implements CompletionStage
     */
    @Override
    public boolean canDecorateReturnType(Class returnType) {
        return CompletionStage.class.isAssignableFrom(returnType);
    }

    /**
     * Decorate a function with a CircuitBreaker.
     *
     * @param circuitBreaker         the CircuitBreaker
     * @param function the function
     * @return the result object
     */
    @Override
    public CheckedFunction0<Object> decorate(CircuitBreaker circuitBreaker, CheckedFunction0<Object> function) {
        return () -> circuitBreaker.executeCompletionStage(() -> {
            try {
                return (CompletionStage<?>) function.apply();
            } catch (Throwable throwable) {
                throw new CompletionException(throwable);
            }
        });
    }
}
