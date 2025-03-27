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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionCellValueTest extends SpreadsheetExpressionFunctionCellTestCase<SpreadsheetExpressionFunctionCellValue, Object> {

    @Override
    public SpreadsheetExpressionFunctionCellValue createBiFunction() {
        return SpreadsheetExpressionFunctionCellValue.INSTANCE;
    }

    @Override
    Object valuePresent() {
        return ExpressionNumberKind.BIG_DECIMAL.create(1);
    }

    @Override
    Object valueAbsent() {
        return null;
    }

    @Override
    SpreadsheetCell setProperty(final SpreadsheetCell cell,
                                final Object value) {
        return cell.setFormula(
                cell.formula()
                        .setExpressionValue(
                                Optional.ofNullable(value)
                        )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "cellValue"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionCellValue> type() {
        return SpreadsheetExpressionFunctionCellValue.class;
    }
}
