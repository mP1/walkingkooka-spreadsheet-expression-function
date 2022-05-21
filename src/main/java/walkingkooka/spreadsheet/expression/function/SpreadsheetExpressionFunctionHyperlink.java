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

import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.spreadsheet.function.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

// https://exceljet.net/excel-functions/excel-hyperlink-function
final class SpreadsheetExpressionFunctionHyperlink extends SpreadsheetExpressionFunction<AbsoluteUrl> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionHyperlink INSTANCE = new SpreadsheetExpressionFunctionHyperlink();

    private SpreadsheetExpressionFunctionHyperlink() {
        super(
                "hyperlink",
                ExpressionFunctionKind.CONVERT_PARAMETERS,
                ExpressionFunctionKind.EVALUATE_PARAMETERS
        );
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
            REFERENCE
    );

    @Override
    public Class<AbsoluteUrl> returnType() {
        return AbsoluteUrl.class;
    }

    @Override
    public AbsoluteUrl apply(final List<Object> parameters,
                             final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final SpreadsheetCellReference reference = REFERENCE.getOrFail(parameters, 0);
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
        final AbsoluteUrl url = context.serverUrl();

        return url.setPath(
                url.path()
                        .append(
                                UrlPath.parse(
                                        "" +
                                        metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID) +
                                                UrlPath.SEPARATOR +
                                                metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME) +
                                                UrlPath.SEPARATOR +
                                                "cell" +
                                                UrlPath.SEPARATOR +
                                                reference
                                )
                        )
        );
    }
}
