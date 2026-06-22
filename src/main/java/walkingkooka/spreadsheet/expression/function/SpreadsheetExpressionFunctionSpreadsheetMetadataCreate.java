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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A function that may be used to create a {@link SpreadsheetMetadata}.
 */
final class SpreadsheetExpressionFunctionSpreadsheetMetadataCreate extends SpreadsheetExpressionFunctionSpreadsheetMetadata<SpreadsheetMetadata> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionSpreadsheetMetadataCreate INSTANCE = new SpreadsheetExpressionFunctionSpreadsheetMetadataCreate();

    /**
     * Private constructor use singleton
     */
    private SpreadsheetExpressionFunctionSpreadsheetMetadataCreate() {
        super("createSpreadsheetMetadata");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int i) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<Locale> LOCALE = ExpressionFunctionParameterName.with("locale")
        .optional(Locale.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        LOCALE
    );

    @Override
    public Class<SpreadsheetMetadata> returnType() {
        return SpreadsheetMetadata.class;
    }

    @Override
    public SpreadsheetMetadata apply(final List<Object> parameters,
                                     final SpreadsheetExpressionEvaluationContext context) {
        Objects.requireNonNull(parameters, "parameters");
        Objects.requireNonNull(context, "context");

        final Optional<Locale> locale = LOCALE.get(
            parameters,
            0
        );

        return context.createMetadata(
            context.userOrFail(),
            locale
        );
    }
}
