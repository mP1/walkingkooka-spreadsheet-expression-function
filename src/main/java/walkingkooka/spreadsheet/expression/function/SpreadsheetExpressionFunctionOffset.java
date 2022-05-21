
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

import walkingkooka.spreadsheet.function.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.ExpressionFunctionKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

// https://exceljet.net/excel-functions/excel-offset-function
final class SpreadsheetExpressionFunctionOffset extends SpreadsheetExpressionFunction<SpreadsheetExpressionReference> {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionOffset INSTANCE = new SpreadsheetExpressionFunctionOffset();

    private SpreadsheetExpressionFunctionOffset() {
        super(
                "offset",
                ExpressionFunctionKind.CONVERT_PARAMETERS,
                ExpressionFunctionKind.EVALUATE_PARAMETERS
        );
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    private final static ExpressionFunctionParameter<ExpressionNumber> ROWS = ExpressionFunctionParameterName.with("rows")
            .required(ExpressionNumber.class);

    private final static ExpressionFunctionParameter<ExpressionNumber> COLUMNS = ExpressionFunctionParameterName.with("columns")
            .required(ExpressionNumber.class);

    private final static ExpressionFunctionParameter<ExpressionNumber> WIDTH = ExpressionFunctionParameterName.with("width")
            .optional(ExpressionNumber.class);

    private final static ExpressionFunctionParameter<ExpressionNumber> HEIGHT = ExpressionFunctionParameterName.with("height")
            .optional(ExpressionNumber.class);

    final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
            CELL_OR_RANGE_REFERENCE,
            ROWS,
            COLUMNS,
            WIDTH,
            HEIGHT
    );

    @Override
    public Class<SpreadsheetExpressionReference> returnType() {
        return SpreadsheetExpressionReference.class;
    }

    @Override
    public SpreadsheetExpressionReference apply(final List<Object> parameters,
                                                final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final SpreadsheetCellReference start = CELL_OR_RANGE_REFERENCE.getOrFail(parameters, 0)
                .toCell();

        final int rows = ROWS.getOrFail(parameters, 1)
                .intValue();

        final int columns = COLUMNS.getOrFail(parameters, 2)
                .intValue();

        final int height = HEIGHT.get(parameters, 3)
                .orElse(DEFAULT_HEIGHT_WIDTH)
                .intValue();

        final int width = WIDTH.get(parameters, 4)
                .orElse(DEFAULT_HEIGHT_WIDTH)
                .intValue();

        final SpreadsheetCellReference topLeft = start.add(
                columns,
                rows
        );
        final SpreadsheetCellReference bottomRight = topLeft.add(
                width - BIAS,
                height - BIAS
        );

        final SpreadsheetCellRange range = topLeft.cellRange(bottomRight);

        return range.width() == 1 && range.height() == 1 ?
                range.toCell() :
                range;
    }

    // only width and height have a bias of 1
    private final static int BIAS = 1;

    private final static ExpressionNumber DEFAULT_HEIGHT_WIDTH = ExpressionNumberKind.DEFAULT.create(BIAS);
}
