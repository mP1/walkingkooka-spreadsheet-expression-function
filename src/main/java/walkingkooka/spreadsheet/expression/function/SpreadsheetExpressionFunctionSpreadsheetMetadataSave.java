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
import java.util.Objects;

/**
 * A function that may be used to save or replace an {@link SpreadsheetMetadata}
 */
final class SpreadsheetExpressionFunctionSpreadsheetMetadataSave extends SpreadsheetExpressionFunctionSpreadsheetMetadata<SpreadsheetMetadata> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionSpreadsheetMetadataSave INSTANCE = new SpreadsheetExpressionFunctionSpreadsheetMetadataSave();

    /**
     * Private constructor use singleton
     */
    private SpreadsheetExpressionFunctionSpreadsheetMetadataSave() {
        super("saveSpreadsheetMetadata");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int i) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<SpreadsheetMetadata> SPREADSHEET_METADATA = ExpressionFunctionParameterName.with("spreadsheetMetadata")
        .required(SpreadsheetMetadata.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        SPREADSHEET_METADATA
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
        System.out.println(parameters.size());
        System.out.println(parameters);
        final SpreadsheetMetadata spreadsheetMetadata = SPREADSHEET_METADATA.getOrFail(
            parameters,
            0
        );

        return context.saveMetadata(
            spreadsheetMetadata
        );
    }
}
