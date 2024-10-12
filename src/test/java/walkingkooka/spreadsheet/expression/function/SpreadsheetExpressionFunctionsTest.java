/*
 * Copyright 2022 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.spreadsheet.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.lang.reflect.Method;
import java.math.MathContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class SpreadsheetExpressionFunctionsTest implements PublicStaticHelperTesting<SpreadsheetExpressionFunctions> {

    @Test
    public void testExpressionFunctionProvider() {
        this.checkEquals(
                Arrays.stream(SpreadsheetExpressionFunctions.class.getDeclaredMethods())
                        .filter(m -> m.getReturnType() == ExpressionFunction.class)
                        .filter(m -> JavaVisibility.PUBLIC == JavaVisibility.of(m))
                        .map(Method::getName)
                        .map(n -> {
                                    // JDK BUG cant have a lambda with switch as the body ???
                                    switch (n) {
                                        case "errorType":
                                            return "Error.Type";
                                        case "formulaText":
                                            return "formulatext";
                                        default:
                                            return n;
                                    }
                                }
                        ).collect(Collectors.toCollection(SortedSets::tree)),
                SpreadsheetExpressionFunctions.expressionFunctionProvider()
                        .expressionFunctionInfos()
                        .stream()
                        .map(i -> i.name().value())
                        .collect(Collectors.toCollection(SortedSets::tree))
        );
    }

    @Test
    public void testPublicStaticMethodsWithoutMathContextParameter() {
        this.publicStaticMethodParametersTypeCheck(MathContext.class);
    }

    @Override
    public Class<SpreadsheetExpressionFunctions> type() {
        return SpreadsheetExpressionFunctions.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
