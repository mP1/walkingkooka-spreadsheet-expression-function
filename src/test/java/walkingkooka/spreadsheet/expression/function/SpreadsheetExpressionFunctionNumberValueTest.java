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
import walkingkooka.convert.Converters;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.environment.AuditInfo;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReferenceLoaders;
import walkingkooka.spreadsheet.store.repo.FakeSpreadsheetStoreRepository;
import walkingkooka.storage.StorageStore;
import walkingkooka.storage.StorageStores;

import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetExpressionFunctionNumberValueTest extends SpreadsheetExpressionFunctionNumberTestCase<SpreadsheetExpressionFunctionNumberValue> {

    @Test
    public void testCustomDecimalSeparatorCustomGroupingSeparator() {
        this.applyAndCheck(
                Lists.of(
                        "1G234D5",
                        'D',
                        'G'
                ),
                this.createContext(),
                EXPRESSION_NUMBER_KIND.create(1234.5)
        );
    }

    @Test
    public void testCustomDecimalSeparator() {
        this.applyAndCheck(
                Lists.of(
                        "1,234D5",
                        'D'
                ),
                this.createContext(),
                EXPRESSION_NUMBER_KIND.create(1234.5)
        );
    }

    @Test
    public void testOnlyNumber() {
        this.applyAndCheck(
                Lists.of(
                        "1,234.5"
                ),
                this.createContext(),
                EXPRESSION_NUMBER_KIND.create(1234.5)
        );
    }

    @Override
    public SpreadsheetExpressionFunctionNumberValue createBiFunction() {
        return SpreadsheetExpressionFunctionNumberValue.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 3;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.parse("1234"))
                .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled5678"))
                .set(SpreadsheetMetadataPropertyName.LOCALE, Locale.ENGLISH)
                .loadFromLocale()
                .set(
                        SpreadsheetMetadataPropertyName.AUDIT_INFO,
                        AuditInfo.with(
                                EmailAddress.parse("creator@example.com"),
                                LocalDateTime.of(1999, 12, 31, 12, 58, 59),
                                EmailAddress.parse("modified@example.com"),
                                LocalDateTime.of(2000, 1, 2, 3, 4, 5)
                        )
                ).set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
                .set(SpreadsheetMetadataPropertyName.DATETIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
                .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
                .set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, EXPRESSION_NUMBER_KIND)
                .set(SpreadsheetMetadataPropertyName.FORMULA_CONVERTER, ConverterSelector.parse("general"))
                .set(SpreadsheetMetadataPropertyName.PRECISION, MathContext.DECIMAL32.getPrecision())
                .set(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP)
                .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("#.###").spreadsheetFormatterSelector())
                .set(SpreadsheetMetadataPropertyName.TEXT_FORMATTER, SpreadsheetPattern.parseTextFormatPattern("@@").spreadsheetFormatterSelector())
                .set(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 20);

        return SpreadsheetExpressionEvaluationContexts.basic(
                Optional.empty(),
                SpreadsheetExpressionReferenceLoaders.fake(),
                Url.parseAbsolute("https://example.com/server"),
                metadata,
                new FakeSpreadsheetStoreRepository() {

                    @Override
                    public StorageStore storage() {
                        return storage;
                    }

                    private final StorageStore storage = StorageStores.tree(STORAGE_STORE_CONTEXT);
                },
                SPREADSHEET_FORMULA_CONVERTER_CONTEXT,
                EXPRESSION_FUNCTION_PROVIDER,
                PROVIDER_CONTEXT
        );
    }

    @Override
    public Class<SpreadsheetExpressionFunctionNumberValue> type() {
        return SpreadsheetExpressionFunctionNumberValue.class;
    }
}
