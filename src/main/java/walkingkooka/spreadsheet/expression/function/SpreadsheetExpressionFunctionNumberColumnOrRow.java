/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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

import walkingkooka.Value;
import walkingkooka.spreadsheet.function.SpreadsheetExpressionFunctionContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;
import java.util.function.Function;

final class SpreadsheetExpressionFunctionNumberColumnOrRow extends SpreadsheetExpressionFunctionNumber {

    /**
     * A function that returns the column for a given argument or defaults to the enclosing cell.
     */
    final static SpreadsheetExpressionFunctionNumberColumnOrRow COLUMN = new SpreadsheetExpressionFunctionNumberColumnOrRow(
            "column",
            SpreadsheetCellReference::column
    );

    /**
     * A function that returns the row for a given argument or defaults to the enclosing cell.
     */
    final static SpreadsheetExpressionFunctionNumberColumnOrRow ROW = new SpreadsheetExpressionFunctionNumberColumnOrRow(
            "row",
            SpreadsheetCellReference::row
    );

    private SpreadsheetExpressionFunctionNumberColumnOrRow(final String name,
                                                           final Function<SpreadsheetCellReference, Value<Integer>> mapper) {
        super(name);
        this.mapper = mapper;
    }

    @Override
    public ExpressionNumber apply(final List<Object> parameters,
                                  final SpreadsheetExpressionFunctionContext context) {
        final int count = parameters.size();
        switch (count) {
            case 0:
            case 1:
                break;
            default:
                throw new IllegalArgumentException("Expected only optional cell reference but got " + count);
        }

        final SpreadsheetCellReference reference = REFERENCE.get(parameters, 0)
                .orElseGet(() -> SpreadsheetExpressionFunctionNumberColumnOrRow.cellReferenceOrFail(context));

        return context.expressionNumberKind()
                .create(
                        BIAS + this.mapper.apply(reference).value()
                );
    }

    private static SpreadsheetCellReference cellReferenceOrFail(final SpreadsheetExpressionFunctionContext context) {
        return context.cell()
                .orElseThrow(() -> new IllegalArgumentException("Context missing cell"))
                .reference();
    }

    /**
     * Lambda that returns either the column or row for a given {@link SpreadsheetCellReference}.
     */
    private final Function<SpreadsheetCellReference, Value<Integer>> mapper;

    @Override
    public List<ExpressionFunctionParameter<?>> parameters() {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<SpreadsheetCellReference> REFERENCE = ExpressionFunctionParameterName.with("reference")
            .setType(SpreadsheetCellReference.class);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(REFERENCE);
}