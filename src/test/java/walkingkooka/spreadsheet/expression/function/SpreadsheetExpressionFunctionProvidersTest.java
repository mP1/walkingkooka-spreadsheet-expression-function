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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class SpreadsheetExpressionFunctionProvidersTest implements PublicStaticHelperTesting<SpreadsheetExpressionFunctionProviders>,
        TreePrintableTesting {

    @Test
    public void testExpressionFunctionProvider() {
        this.checkEquals(
                Arrays.stream(SpreadsheetExpressionFunctions.class.getDeclaredMethods())
                        .filter(m -> m.getReturnType() == ExpressionFunction.class)
                        .filter(m -> m.getParameterTypes().length == 0)
                        .map(Method::getName)
                        .map(n -> {
                                    // JDK BUG cant have a lambda with switch as the body ???
                                    switch (n) {
                                        case "bitAnd":
                                            return "bitand";
                                        case "bitOr":
                                            return "bitor";
                                        case "bitXor":
                                            return "bitxor";
                                        case "charFunction":
                                            return "char";
                                        case "falseFunction":
                                            return "false";
                                        case "formulaText":
                                            return "formulatext";
                                        case "ifFunction":
                                            return "if";
                                        case "intFunction":
                                            return "int";
                                        case "isoWeekNum":
                                            return "isoweeknum";
                                        case "nullFunction":
                                            return "null";
                                        case "switchFunction":
                                            return "switch";
                                        case "trueFunction":
                                            return "true";
                                        case "weekDay":
                                            return "weekday";
                                        case "weekNum":
                                            return "weeknum";
                                        default:
                                            return n;
                                    }
                                }
                        ).collect(Collectors.toCollection(SortedSets::tree)),
                (Object) SpreadsheetExpressionFunctionProviders.expressionFunctionProvider(CaseSensitivity.SENSITIVE)
                        .expressionFunctionInfos()
                        .stream()
                        .map(i -> i.name().value())
                        .collect(Collectors.toCollection(SortedSets::tree))
        );
    }

    @Override
    public Class<SpreadsheetExpressionFunctionProviders> type() {
        return SpreadsheetExpressionFunctionProviders.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
