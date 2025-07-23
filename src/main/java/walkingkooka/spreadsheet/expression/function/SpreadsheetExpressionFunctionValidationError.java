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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetError;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.validation.ValidationError;

import java.util.List;

/**
 * A function that supports creation of a {@link ValidationError} from a string.
 * <pre>
 * ValidationError("#N/A HelloError")
 * </pre>
 */
final class SpreadsheetExpressionFunctionValidationError extends SpreadsheetExpressionFunction<ValidationError<SpreadsheetCellReference>> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionValidationError INSTANCE = new SpreadsheetExpressionFunctionValidationError();

    private SpreadsheetExpressionFunctionValidationError() {
        super("validationError");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetError> ERROR = ExpressionFunctionParameterName.with("error")
        .required(SpreadsheetError.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
        );

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        ERROR
    );

    @Override
    public Class<ValidationError<SpreadsheetCellReference>> returnType() {
        return Cast.to(ValidationError.class);
    }

    @Override
    public ValidationError<SpreadsheetCellReference> apply(final List<Object> parameters,
                                                           final SpreadsheetExpressionEvaluationContext context) {
        final SpreadsheetError spreadsheetError = ERROR.getOrFail(parameters, 0);

        return spreadsheetError.toValidationError(
            context.cellOrFail()
                .reference()
        );
    }
}
