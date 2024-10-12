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
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionLambdaTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionLambda, ExpressionFunction<?, SpreadsheetExpressionEvaluationContext>>
        implements ToStringTesting<SpreadsheetExpressionFunctionLambda> {

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testZeroParametersMissingExpressionFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetExpressionFunctionLambda.INSTANCE.parameters(0)
        );
        this.checkEquals(
                "Missing last parameter with expression",
                thrown.getMessage(),
                "message"
        );
    }

    @Test
    public void testOneParametersOnlyExpression() {
        this.checkEquals(
                Lists.of(
                        ExpressionFunctionParameterName.with("expression")
                                .required(Expression.class)
                                .setKinds(ExpressionFunctionParameter.NO_KINDS)
                ),
                SpreadsheetExpressionFunctionLambda.INSTANCE.parameters(1)
        );
    }

    @Test
    public void testThreeParameters() {
        this.checkEquals(
                Lists.of(
                        ExpressionFunctionParameterName.with("parameter-1")
                                .required(SpreadsheetLabelName.class)
                                .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE),
                        ExpressionFunctionParameterName.with("parameter-2")
                                .required(SpreadsheetLabelName.class)
                                .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE),
                        ExpressionFunctionParameterName.with("expression")
                                .required(Expression.class)
                                .setKinds(ExpressionFunctionParameter.NO_KINDS)
                ),
                SpreadsheetExpressionFunctionLambda.INSTANCE.parameters(3)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                SpreadsheetExpressionFunctionLambda.INSTANCE,
                "lambda"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionLambda createBiFunction() {
        return SpreadsheetExpressionFunctionLambda.INSTANCE;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionLambda> type() {
        return Cast.to(SpreadsheetExpressionFunctionLambda.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}
