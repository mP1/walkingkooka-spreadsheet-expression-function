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
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@link SpreadsheetExpressionFunctionNumber} that wraps another function, using the 2nd parameter as a condition to filter the first parameter
 * which may be a value or a {@link List} of values from a range etc.
 */
final class SpreadsheetExpressionFunctionNumberIf extends SpreadsheetExpressionFunctionNumber {

    static SpreadsheetExpressionFunctionNumberIf averageIf() {
        return new SpreadsheetExpressionFunctionNumberIf(
            "averageIf",
            SpreadsheetExpressionFunctions.average()
        );
    }

    static SpreadsheetExpressionFunctionNumberIf countIf() {
        return new SpreadsheetExpressionFunctionNumberIf(
            "countIf",
            SpreadsheetExpressionFunctions.count()
        );
    }

    static SpreadsheetExpressionFunctionNumberIf maxIf() {
        return new SpreadsheetExpressionFunctionNumberIf(
            "maxIf",
            SpreadsheetExpressionFunctions.max()
        );
    }

    static SpreadsheetExpressionFunctionNumberIf minIf() {
        return new SpreadsheetExpressionFunctionNumberIf(
            "minIf",
            SpreadsheetExpressionFunctions.min()
        );
    }

    static SpreadsheetExpressionFunctionNumberIf sumIf() {
        return new SpreadsheetExpressionFunctionNumberIf(
            "sumIf",
            SpreadsheetExpressionFunctions.sum()
        );
    }

    private SpreadsheetExpressionFunctionNumberIf(final String name,
                                                  final ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> function) {
        super(name);
        this.function = function;
    }

    @Override
    public ExpressionNumber apply(final List<Object> parameters,
                                  final SpreadsheetExpressionEvaluationContext context) {
        final Object value = ExpressionFunctionParameter.VALUE.getOrFail(parameters, 0);

        final Predicate<Object> criteria = SpreadsheetExpressionFunctionNumberIfPredicate.with(
            CRITERIA.getOrFail(parameters, 1),
            context
        );

        final ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> function = this.function;

        return function.apply(
            context.prepareParameters(
                Cast.to(function),
                this.filterParameters(
                    value,
                    criteria,
                    context
                )
            ),
            context
        );
    }

    private final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameter.VALUE
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    private final static ExpressionFunctionParameter<Object> CRITERIA = ExpressionFunctionParameterName.with("criteria")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    /**
     * Filters the parameter values using the filter.
     */
    private List<Object> filterParameters(final Object value,
                                          final Predicate<Object> filter,
                                          final SpreadsheetExpressionEvaluationContext context) {
        return this.filterParameters0(
            value instanceof List ?
                Cast.to(value) :
                Lists.of(value),
            filter
        );
    }

    /**
     * Filter the parameters using the filter created using the criteria.
     */
    private List<Object> filterParameters0(final List<Object> value,
                                           final Predicate<Object> filter) {
        return value.stream()
            .filter(filter)
            .collect(Collectors.toList());
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        VALUE,
        CRITERIA
    );

    private final ExpressionFunction<ExpressionNumber, SpreadsheetExpressionEvaluationContext> function;
}
