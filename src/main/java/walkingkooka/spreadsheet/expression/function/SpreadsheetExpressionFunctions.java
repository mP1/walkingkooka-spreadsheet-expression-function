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

/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.function.SpreadsheetExpressionFunctionContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.function.Consumer;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class SpreadsheetExpressionFunctions implements PublicStaticHelper {

    /**
     * Visit all {@link ExpressionFunction functions}.
     */
    public static void visit(final Consumer<ExpressionFunction<?, ?>> consumer) {
        Lists.of(
                column(),
                columns(),
                row(),
                rows()
        ).forEach(consumer);
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnOrRow#COLUMN}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionFunctionContext> column() {
        return SpreadsheetExpressionFunctionNumberColumnOrRow.COLUMN;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnsOrRows#COLUMNS}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionFunctionContext> columns() {
        return SpreadsheetExpressionFunctionNumberColumnsOrRows.COLUMNS;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnOrRow#ROW}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionFunctionContext> row() {
        return SpreadsheetExpressionFunctionNumberColumnOrRow.ROW;
    }

    /**
     * {@see SpreadsheetExpressionFunctionNumberColumnsOrRows#ROWS}
     */
    public static ExpressionFunction<ExpressionNumber, SpreadsheetExpressionFunctionContext> rows() {
        return SpreadsheetExpressionFunctionNumberColumnsOrRows.ROWS;
    }

    /**
     * Stops creation
     */
    private SpreadsheetExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
