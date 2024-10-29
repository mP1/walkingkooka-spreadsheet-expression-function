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
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public abstract class SpreadsheetExpressionFunctionCellTestCase<F extends SpreadsheetExpressionFunctionCell<T>, T> extends SpreadsheetExpressionFunctionTestCase<F, T> {

    SpreadsheetExpressionFunctionCellTestCase() {
        super();
    }

    @Test
    public void testApplyWhenPropertyPresent() {
        this.applyAndCheck2(
                this.valuePresent()
        );
    }

    abstract T valuePresent();

    @Test
    public void testApplyWhenPropertyAbsent() {
        this.applyAndCheck2(
                this.valueAbsent()
        );
    }

    abstract T valueAbsent();

    private void applyAndCheck2(final T value) {
        this.applyAndCheck(
                Lists.empty(),
                new FakeSpreadsheetExpressionEvaluationContext() {
                    @Override
                    public Optional<SpreadsheetCell> cell() {
                        return Optional.of(
                                setProperty(
                                        SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                                        value
                                )
                        );
                    }

                    @Override
                    public String toString() {
                        return "cell: " + this.cell();
                    }
                },
                value
        );
    }

    abstract SpreadsheetCell setProperty(final SpreadsheetCell cell,
                                         final T value);

    @Override
    public final int minimumParameterCount() {
        return 0;
    }


    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return SpreadsheetExpressionEvaluationContexts.fake();
    }

    // class............................................................................................................

    @Override
    public final String typeNamePrefix() {
        return SpreadsheetExpressionFunctionCell.class.getSimpleName();
    }
}
