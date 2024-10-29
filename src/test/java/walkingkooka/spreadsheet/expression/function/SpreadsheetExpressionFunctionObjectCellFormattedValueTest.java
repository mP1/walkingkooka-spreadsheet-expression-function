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
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.format.SpreadsheetText;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionObjectCellFormattedValueTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectCellFormattedValue> {

    @Test
    public void testApplyWhenFormattedValuePresent() {
        this.applyAndCheck2(
                SpreadsheetText.with(
                        "Hello123"
                ).setColor(
                        Optional.of(Color.parse("#456789"))
                ).toTextNode()
        );
    }

    @Test
    public void testApplyWhenFormattedValueAbsent() {
        this.applyAndCheck2(null);
    }

    private void applyAndCheck2(final TextNode formattedValue) {
        this.applyAndCheck(
                Lists.empty(),
                new FakeSpreadsheetExpressionEvaluationContext() {
                    @Override
                    public Optional<SpreadsheetCell> cell() {
                        return Optional.of(
                                SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                                        .setFormattedValue(
                                                Optional.ofNullable(formattedValue)
                                        )
                        );
                    }

                    @Override
                    public String toString() {
                        return "cell: " + this.cell();
                    }
                },
                formattedValue
        );
    }

    @Override
    public SpreadsheetExpressionFunctionObjectCellFormattedValue createBiFunction() {
        return SpreadsheetExpressionFunctionObjectCellFormattedValue.INSTANCE;
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
                "cellFormattedValue"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionObjectCellFormattedValue> type() {
        return SpreadsheetExpressionFunctionObjectCellFormattedValue.class;
    }
}
