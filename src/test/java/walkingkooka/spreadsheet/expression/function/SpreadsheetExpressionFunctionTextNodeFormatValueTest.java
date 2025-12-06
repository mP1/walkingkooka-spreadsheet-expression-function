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
import walkingkooka.locale.LocaleContexts;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.engine.SpreadsheetMetadataMode;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReferenceLoaders;
import walkingkooka.spreadsheet.store.repo.FakeSpreadsheetStoreRepository;
import walkingkooka.storage.Storage;
import walkingkooka.storage.Storages;
import walkingkooka.storage.expression.function.StorageExpressionEvaluationContext;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;

public final class SpreadsheetExpressionFunctionTextNodeFormatValueTest extends SpreadsheetExpressionFunctionTextNodeTestCase<SpreadsheetExpressionFunctionTextNodeFormatValue> {

    @Test
    public void testApplyWithTextFormatter() {
        this.applyAndCheck2(
            Lists.of(
                SpreadsheetFormatterSelector.DEFAULT_TEXT_FORMAT.setValueText("@@"),
                "Hello"
            ),
            TextNode.text("HelloHello")
        );
    }

    // test related factories...........................................................................................

    @Override
    public SpreadsheetExpressionFunctionTextNodeFormatValue createBiFunction() {
        return SpreadsheetExpressionFunctionTextNodeFormatValue.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        final Locale locale = Locale.ENGLISH;

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.parse("1234"))
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled5678"))
            .set(SpreadsheetMetadataPropertyName.LOCALE, locale)
            .loadFromLocale(
                LocaleContexts.jre(locale)
            ).set(
                SpreadsheetMetadataPropertyName.AUDIT_INFO,
                AuditInfo.create(
                    EmailAddress.parse("creator@example.com"),
                    LocalDateTime.of(1999, 12, 31, 12, 58, 59)
                )
            ).set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
            .set(SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
            .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
            .set(
                SpreadsheetMetadataPropertyName.ERROR_FORMATTER,
                SpreadsheetFormatterSelector.parse("badge-error default-text")
            ).set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, EXPRESSION_NUMBER_KIND)
            .set(
                SpreadsheetMetadataPropertyName.FORMULA_CONVERTER,
                ConverterSelector.parse("collection (text, number, basic, spreadsheet-value, error-throwing)")
            ).set(
                SpreadsheetMetadataPropertyName.FORMATTING_CONVERTER,
                ConverterSelector.parse("basic")
            ).set(SpreadsheetMetadataPropertyName.GENERAL_NUMBER_FORMAT_DIGIT_COUNT, 8)
            .set(SpreadsheetMetadataPropertyName.PRECISION, MathContext.DECIMAL32.getPrecision())
            .set(SpreadsheetMetadataPropertyName.ROUNDING_MODE, RoundingMode.HALF_UP)
            .set(SpreadsheetMetadataPropertyName.NUMBER_FORMATTER, SpreadsheetPattern.parseNumberFormatPattern("#.###").spreadsheetFormatterSelector())
            .set(SpreadsheetMetadataPropertyName.TEXT_FORMATTER, SpreadsheetPattern.parseTextFormatPattern("@@").spreadsheetFormatterSelector())
            .set(SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR, 20);

        return SpreadsheetExpressionEvaluationContexts.basic(
            Url.parseAbsolute("https://example.com/server"),
            metadata,
            SpreadsheetMetadataMode.FORMULA,
            new FakeSpreadsheetStoreRepository() {

                @Override
                public Storage<StorageExpressionEvaluationContext> storage() {
                    return storage;
                }

                private final Storage<StorageExpressionEvaluationContext> storage = Storages.tree();
            },
            ENVIRONMENT_CONTEXT,
            SpreadsheetExpressionEvaluationContext.NO_CELL,
            SpreadsheetExpressionReferenceLoaders.fake(),
            SPREADSHEET_LABEL_NAME_RESOLVER,
            LOCALE_CONTEXT,
            TERMINAL_CONTEXT,
            SPREADSHEET_PROVIDER,
            PROVIDER_CONTEXT
        );
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionTextNodeFormatValue> type() {
        return SpreadsheetExpressionFunctionTextNodeFormatValue.class;
    }
}
