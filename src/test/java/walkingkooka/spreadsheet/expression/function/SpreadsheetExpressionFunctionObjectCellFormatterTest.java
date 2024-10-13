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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionObjectCellFormatterTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectCellFormatter> {

    @Test
    public void testApplyWhenFormatterPresent() {
        final SpreadsheetFormatterSelector formatter = SpreadsheetFormatterSelector.parse("date-time-format dd/mm/yyyy");

        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1+2")
        ).setFormatter(
                Optional.of(formatter)
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
                formatter
        );
    }

    @Test
    public void testApplyValueAbsent() {
        final SpreadsheetCell cell = SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1+2")
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
                null
        );
    }

    @Override
    public SpreadsheetExpressionFunctionObjectCellFormatter createBiFunction() {
        return SpreadsheetExpressionFunctionObjectCellFormatter.INSTANCE;
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
                "cellFormatter"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionObjectCellFormatter> type() {
        return SpreadsheetExpressionFunctionObjectCellFormatter.class;
    }
}
