
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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.ExpressionFunctionTesting;

import java.util.Locale;
import java.util.Optional;

public abstract class SpreadsheetExpressionFunctionTestCase<F extends SpreadsheetExpressionFunction<T>, T>
        implements ExpressionFunctionTesting<F, T, SpreadsheetExpressionEvaluationContext>,
        ClassTesting2<F>,
        TypeNameTesting<F> {

    final static SpreadsheetCellReference REFERENCE = SpreadsheetSelection.parseCell("Z99");

    final static SpreadsheetCellReference LOAD_CELL_REFERENCE = SpreadsheetSelection.parseCell("M31");

    final static String LOAD_FORMULA_TEXT = "='loaded formulatext result";

    final static SpreadsheetCell LOAD_CELL = SpreadsheetCell.with(
            LOAD_CELL_REFERENCE,
            SpreadsheetFormula.EMPTY.setText(LOAD_FORMULA_TEXT)
    );

    final static SpreadsheetCell CELL_EMPTY_FORMULA = SpreadsheetCell.with(
            SpreadsheetSelection.parseCell("E5"),
            SpreadsheetFormula.EMPTY.setText("")
    );

    final static SpreadsheetFormula FORMULA = SpreadsheetFormula.EMPTY
            .setText("=1+2");

    final static SpreadsheetCell CELL = SpreadsheetCell.with(
            REFERENCE,
            FORMULA
    );

    final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    final static SpreadsheetId ID = SpreadsheetId.with(0x123);

    final static SpreadsheetName NAME = SpreadsheetName.with("spreadsheet-name-456");

    final static AbsoluteUrl SERVER_URL = Url.parseAbsolute("https://example.com/path789");

    SpreadsheetExpressionFunctionTestCase() {
        super();
    }

    @Test
    public final void testIsPure() {
        final F function = this.createBiFunction();
        final String name = function.name()
                .get()
                .value()
                .toLowerCase();

        this.isPureAndCheck(
                function,
                ExpressionEvaluationContexts.fake(),
                !(name.equals("cell") || name.equals("offset"))
        );
    }

    @Override
    public final SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {
            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return EXPRESSION_NUMBER_KIND;
            }

            @Override
            public Optional<SpreadsheetCell> cell() {
                return Optional.of(
                        CELL
                );
            }

            @Override
            public boolean isText(final Object value) {
                return value instanceof Character || value instanceof String;
            }

            @Override
            public Optional<SpreadsheetCell> loadCell(final SpreadsheetCellReference cell) {
                if (LOAD_CELL_REFERENCE.equals(cell)) {
                    return Optional.of(LOAD_CELL);
                }
                if (CELL_EMPTY_FORMULA.reference().equals(cell)) {
                    return Optional.of(CELL_EMPTY_FORMULA);
                }
                return Optional.empty();
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadata.EMPTY
                        .set(SpreadsheetMetadataPropertyName.LOCALE, Locale.forLanguageTag("EN-AU"))
                        .loadFromLocale()
                        .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, ID)
                        .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, NAME);
            }

            @Override
            public AbsoluteUrl serverUrl() {
                return SERVER_URL;
            }

            @Override
            public String toString() {
                return EXPRESSION_NUMBER_KIND + " " + CELL;
            }
        };
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
