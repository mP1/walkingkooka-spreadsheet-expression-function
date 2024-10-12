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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionObjectCellValueTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectCellValue> {

    @Test
    public void testApplyValuePresent() {
        final Object value = 345;

        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1+2")
                        .setValue(
                                Optional.of(value)
                        )
        );
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
                value
        );
    }

    @Test
    public void testApplyValueAbsent() {
        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1+2")
                        .setValue(
                                Optional.empty()
                        )
        );

        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> this.createBiFunction()
                        .apply(
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
                                }
                        )
        );
        this.checkEquals(
                "Missing cell value for A1",
                thrown.getMessage()
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "cellValue"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionObjectCellValue createBiFunction() {
        return SpreadsheetExpressionFunctionObjectCellValue.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionObjectCellValue> type() {
        return SpreadsheetExpressionFunctionObjectCellValue.class;
    }
}
