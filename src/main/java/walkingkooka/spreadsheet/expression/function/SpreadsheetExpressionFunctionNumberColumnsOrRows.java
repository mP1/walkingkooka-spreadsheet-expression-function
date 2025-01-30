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

import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;
import java.util.function.Function;

// https://exceljet.net/excel-functions/excel-columns-function
// https://exceljet.net/excel-functions/excel-rows-function
final class SpreadsheetExpressionFunctionNumberColumnsOrRows extends SpreadsheetExpressionFunctionNumber {

    /**
     * A function that returns the column count for a given argument
     */
    final static SpreadsheetExpressionFunctionNumberColumnsOrRows COLUMNS = new SpreadsheetExpressionFunctionNumberColumnsOrRows(
            "columns",
            (r) -> r.columnRange().count()
    );

    /**
     * A function that returns the row count for a given argument
     */
    final static SpreadsheetExpressionFunctionNumberColumnsOrRows ROWS = new SpreadsheetExpressionFunctionNumberColumnsOrRows(
            "rows",
            (r) -> r.rowRange().count()
    );

    private SpreadsheetExpressionFunctionNumberColumnsOrRows(final String name,
                                                             final Function<SpreadsheetCellRangeReference, Long> mapper) {
        super(name);
        this.mapper = mapper;
    }

    @Override
    public ExpressionNumber apply(final List<Object> parameters,
                                  final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final SpreadsheetSelection reference = CELL_OR_RANGE_REFERENCE.getOrFail(parameters, 0);

        return context.expressionNumberKind()
                .create(
                        reference.isCell() ?
                                1L :
                                this.mapper.apply((SpreadsheetCellRangeReference) reference)
                );
    }

    /**
     * Lambda that returns either the column or row count for a given {@link SpreadsheetSelection}.
     */
    private final Function<SpreadsheetCellRangeReference, Long> mapper;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(CELL_OR_RANGE_REFERENCE);
}
