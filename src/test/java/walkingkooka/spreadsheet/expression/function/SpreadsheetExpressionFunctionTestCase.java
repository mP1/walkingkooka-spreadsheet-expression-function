
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
import walkingkooka.convert.Converters;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.FakeSpreadsheetExpressionReferenceLoader;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.storage.StorageStores;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.ExpressionFunctionTesting;

import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

public abstract class SpreadsheetExpressionFunctionTestCase<F extends SpreadsheetExpressionFunction<T>, T>
        implements ExpressionFunctionTesting<F, T, SpreadsheetExpressionEvaluationContext>,
        ClassTesting2<F>,
        TypeNameTesting<F>,
        SpreadsheetMetadataTesting {

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

    final static ExpressionNumberKind KIND = SpreadsheetMetadataTesting.EXPRESSION_NUMBER_KIND;

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

    final SpreadsheetExpressionEvaluationContext createContext0() {
        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.parse("1234"))
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled5678"))
                .set(SpreadsheetMetadataPropertyName.LOCALE, Locale.ENGLISH)
                .loadFromLocale()
                .set(SpreadsheetMetadataPropertyName.CREATED_BY, EmailAddress.parse("creator@example.com"))
                .set(SpreadsheetMetadataPropertyName.CREATED_TIMESTAMP, LocalDateTime.now())
                .set(SpreadsheetMetadataPropertyName.MODIFIED_BY, EmailAddress.parse("modified@example.com"))
                .set(SpreadsheetMetadataPropertyName.MODIFIED_TIMESTAMP, LocalDateTime.now())
                .set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
                .set(SpreadsheetMetadataPropertyName.DATETIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
                .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
                .set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, KIND)
                .set(SpreadsheetMetadataPropertyName.FORMULA_CONVERTER, ConverterSelector.parse("general"))
                .set(SpreadsheetMetadataPropertyName.PRECISION, MathContext.DECIMAL32.getPrecision())
                .set(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP)
                .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("#.###").spreadsheetFormatterSelector())
                .set(SpreadsheetMetadataPropertyName.TEXT_FORMATTER, SpreadsheetPattern.parseTextFormatPattern("@@").spreadsheetFormatterSelector())
                .set(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 20);

        return SpreadsheetExpressionEvaluationContexts.basic(
                Optional.of(CELL),
                new FakeSpreadsheetExpressionReferenceLoader() {
                    @Override
                    public Optional<SpreadsheetCell> loadCell(final SpreadsheetCellReference cell,
                                                              final SpreadsheetExpressionEvaluationContext context) {
                        if (LOAD_CELL_REFERENCE.equals(cell)) {
                            return Optional.of(LOAD_CELL);
                        }
                        if (CELL_EMPTY_FORMULA.reference().equals(cell)) {
                            return Optional.of(CELL_EMPTY_FORMULA);
                        }
                        return Optional.empty();
                    }
                },
                SERVER_URL,
                metadata,
                StorageStores.tree(STORAGE_STORE_CONTEXT),
                SPREADSHEET_FORMULA_CONVERTER_CONTEXT,
                EXPRESSION_FUNCTION_PROVIDER,
                PROVIDER_CONTEXT
        );
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
