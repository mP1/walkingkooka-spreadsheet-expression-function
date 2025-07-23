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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.Expression;

public final class SpreadsheetExpressionFunctionObjectEvalTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectEval> {

    @Test
    public void testApplyEvalExpression() {
        this.applyAndCheck2(
            Lists.of(
                Expression.add(
                    Expression.value(
                        EXPRESSION_NUMBER_KIND.create(11)
                    ),
                    Expression.value(
                        EXPRESSION_NUMBER_KIND.create(22)
                    )
                )
            ),
            EXPRESSION_NUMBER_KIND.create(
                11 + 22
            )
        );
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public SpreadsheetExpressionFunctionObjectEval createBiFunction() {
        return SpreadsheetExpressionFunctionObjectEval.INSTANCE;
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionObjectEval> type() {
        return SpreadsheetExpressionFunctionObjectEval.class;
    }
}
