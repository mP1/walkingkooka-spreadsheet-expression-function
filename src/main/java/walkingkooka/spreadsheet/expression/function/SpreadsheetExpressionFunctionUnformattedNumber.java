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
import walkingkooka.spreadsheet.convert.SpreadsheetConverters;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.Optional;

/**
 * A {@link ExpressionFunction} that executes the wraps another {@link ExpressionFunction}, passing a context that
 * ensures any parameters of type {@link Number} are converted to a {@link String} ignoring the {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName#NUMBER_FORMATTER}.
 */
final class SpreadsheetExpressionFunctionUnformattedNumber<T> implements ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> {

    static <T> SpreadsheetExpressionFunctionUnformattedNumber<T> with(final ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> function) {
        return new SpreadsheetExpressionFunctionUnformattedNumber<>(function);
    }

    private SpreadsheetExpressionFunctionUnformattedNumber(final ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> function) {
        super();
        this.function = function.setName(
            function.name()
                .map(n -> n.setCaseSensitivity(SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY))
        );
    }

    @Override
    public Optional<ExpressionFunctionName> name() {
        return this.function.name();
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return this.function.parameters(count);
    }

    @Override
    public Class<T> returnType() {
        return this.function.returnType();
    }

    @Override
    public T apply(final List<Object> parameters,
                   final SpreadsheetExpressionEvaluationContext context) {
        return this.apply0(
            parameters,
            SpreadsheetExpressionEvaluationContexts.converter(
                SpreadsheetConverters.unformattedNumber().cast(SpreadsheetExpressionEvaluationContext.class),
                context
            )
        );
    }

    private T apply0(final List<Object> parameters,
                     final SpreadsheetExpressionEvaluationContext context) {
        final ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> function = this.function;
        return function.apply(
            context.prepareParameters(
                Cast.to(function),
                parameters
            ),
            context
        );
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return this.function.isPure(context);
    }

    private final ExpressionFunction<T, SpreadsheetExpressionEvaluationContext> function;

    @Override
    public String toString() {
        return this.function.toString();
    }
}
