
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

import walkingkooka.predicate.Predicates;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This function is intended to be used within a search query expression to match text using a GLOB pattern
 * against a given {@link walkingkooka.spreadsheet.SpreadsheetCell} property in text form.
 */
final class SpreadsheetExpressionFunctionBooleanTextMatch extends SpreadsheetExpressionFunctionBoolean {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionBooleanTextMatch INSTANCE = new SpreadsheetExpressionFunctionBooleanTextMatch();

    private SpreadsheetExpressionFunctionBooleanTextMatch() {
        super("textMatch");
    }

    @Override
    public Boolean apply(final List<Object> parameters,
                         final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final String pattern = PATTERN.getOrFail(parameters, 0);

        final Predicate<CharSequence> predicate = Predicates.any(
                Arrays.stream(
                                pattern.split(" "))
                        .filter(s -> s.length() > 0)
                        .map(SpreadsheetExpressionFunctionBooleanTextMatch::predicate)
                        .collect(Collectors.<Predicate<CharSequence>>toList())
        );

        return predicate.test(
                VALUE.getOrFail(
                        parameters,
                        1
                )
        );
    }

    private static Predicate<CharSequence> predicate(final String pattern) {
        return CaseSensitivity.INSENSITIVE.globPattern(
                pattern,
                '\\'
        );
    }

    final static ExpressionFunctionParameter<String> PATTERN = ExpressionFunctionParameterName.with("pattern")
            .required(String.class)
            .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<String> VALUE = ExpressionFunctionParameterName.with("value")
            .required(String.class)
            .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
            PATTERN,
            VALUE
    );
}
