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

import walkingkooka.net.Url;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.UrlPath;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that returns a {@link Url} for the given or current {@link walkingkooka.spreadsheet.meta.SpreadsheetId}.
 */
final class SpreadsheetExpressionFunctionUrlSpreadsheetUrl extends SpreadsheetExpressionFunctionUrl {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionUrlSpreadsheetUrl INSTANCE = new SpreadsheetExpressionFunctionUrlSpreadsheetUrl();

    private SpreadsheetExpressionFunctionUrlSpreadsheetUrl() {
        super("spreadsheetUrl");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<SpreadsheetId> SPREADSHEET_ID = ExpressionFunctionParameterName.with("spreadsheetId")
        .optional(SpreadsheetId.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        SPREADSHEET_ID
    );

    @Override
    Url apply0(final List<Object> parameters,
               final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final SpreadsheetId spreadsheetId = SPREADSHEET_ID.get(
            parameters,
            0
        ).orElse(
            context.spreadsheetIdOrFail()
        );

        return Url.EMPTY_RELATIVE_URL.setFragment(
            UrlFragment.with(
                UrlPath.SEPARATOR.string() + spreadsheetId
            )
        );
    }
}
