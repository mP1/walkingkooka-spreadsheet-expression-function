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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that may be used to get a {@link SpreadsheetParserSelector}.
 */
final class SpreadsheetExpressionFunctionGetParser extends SpreadsheetExpressionFunction<SpreadsheetParserSelector> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionGetParser INSTANCE = new SpreadsheetExpressionFunctionGetParser();

    private SpreadsheetExpressionFunctionGetParser() {
        super("getParser");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetParserSelector> PARSER = ExpressionFunctionParameterName.with("parser")
        .required(SpreadsheetParserSelector.class)
        .setKinds(
            ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES
        );

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        PARSER
    );

    @Override
    public Class<SpreadsheetParserSelector> returnType() {
        return SpreadsheetParserSelector.class;
    }

    @Override
    public SpreadsheetParserSelector apply(final List<Object> parameters,
                                           final SpreadsheetExpressionEvaluationContext context) {
        return PARSER.getOrFail(parameters, 0, context);

    }
}
