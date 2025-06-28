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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;
import walkingkooka.tree.expression.ExpressionNumber;

import java.util.Locale;
import java.util.Optional;

public abstract class SpreadsheetExpressionFunctionCellTestCase<F extends SpreadsheetExpressionFunctionCell<T>, T> extends SpreadsheetExpressionFunctionTestCase<F, T>
        implements ConverterTesting,
        SpreadsheetMetadataTesting {

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

    @Test
    public final void testGeneralConverterValueToString() {
        Object value = this.valuePresent();
        if (value instanceof Optional) {
            final Optional<?> optional = Cast.to(value);
            value = optional.get();
        }

        this.convertAndCheck(
                METADATA_EN_AU.generalConverter(
                        SPREADSHEET_FORMATTER_PROVIDER,
                        SPREADSHEET_PARSER_PROVIDER,
                        PROVIDER_CONTEXT
                ),
                value,
                String.class,
                SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_CONTEXT,
                value instanceof Locale ?
                        ((Locale) value).toLanguageTag() :
                        value instanceof HasText ?
                                HasText.class.cast(value).text() :
                                value instanceof ExpressionNumber ?
                                        "1." :
                                        "*Something went wrong here*"
        );
    }

    // class............................................................................................................

    @Override
    public final String typeNamePrefix() {
        return SpreadsheetExpressionFunctionCell.class.getSimpleName();
    }
}
