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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionObjectCellStyleTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectCellStyle> {

    @Test
    public void testApply() {
        final TextStyle style = TextStyle.EMPTY.set(
                TextStylePropertyName.TEXT_ALIGN,
                TextAlign.LEFT
        );

        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                .setStyle(style);
        this.applyAndCheck(
                Lists.empty(),
                new FakeSpreadsheetExpressionEvaluationContext() {
                    @Override
                    public Optional<SpreadsheetCell> cell() {
                        return Optional.of(cell);
                    }

                    @Override
                    public String toString() {
                        return "cell: " + this.cell();
                    }
                },
                style
        );
    }

    @Override
    public SpreadsheetExpressionFunctionObjectCellStyle createBiFunction() {
        return SpreadsheetExpressionFunctionObjectCellStyle.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "cellStyle"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionObjectCellStyle> type() {
        return SpreadsheetExpressionFunctionObjectCellStyle.class;
    }
}
