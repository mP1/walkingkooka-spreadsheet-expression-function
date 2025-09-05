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
import walkingkooka.environment.AuditInfo;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReferenceLoaders;
import walkingkooka.spreadsheet.store.repo.FakeSpreadsheetStoreRepository;
import walkingkooka.storage.StorageStore;
import walkingkooka.storage.StorageStores;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.validation.form.FormHandlerContexts;

import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetExpressionFunctionNumberIfPredicateTest implements PredicateTesting,
    SpreadsheetMetadataTesting {

    @Test
    public void testEqualsNumber() {
        this.testTrue(
            123,
            123
        );
    }

    @Test
    public void testEqualsNumber2() {
        this.testFalse(
            123,
            4444
        );
    }

    @Test
    public void testEqualsEqualsSignString() {
        this.testTrue(
            123,
            "=123"
        );
    }

    // less than .......................................................................................................

    @Test
    public void testEqualsEqualsSignString2() {
        this.testFalse(
            123,
            "=456"
        );
    }

    @Test
    public void testLessThanString() {
        this.testTrue(
            123,
            "<456"
        );
    }

    @Test
    public void testLessThanString2() {
        this.testFalse(
            999,
            "<456"
        );
    }

    // less than equal..................................................................................................

    @Test
    public void testLessThanEqualsString() {
        this.testTrue(
            123,
            "<=456"
        );
    }

    @Test
    public void testLessThanEqualsString2() {
        this.testTrue(
            456,
            "<=456"
        );
    }

    @Test
    public void testLessThanEqualsString3() {
        this.testFalse(
            999,
            "<=456"
        );
    }

    // greater than....................................................................................................

    @Test
    public void testGreaterThanString() {
        this.testTrue(
            789,
            ">456"
        );
    }

    @Test
    public void testGreaterThanString2() {
        this.testFalse(
            456,
            ">456"
        );
    }

    @Test
    public void testGreaterThanString3() {
        this.testFalse(
            123,
            ">456"
        );
    }

    // greater than equal...............................................................................................

    @Test
    public void testGreaterThanEqualsString() {
        this.testTrue(
            789,
            ">=456"
        );
    }

    @Test
    public void testGreaterThanEqualsString2() {
        this.testTrue(
            456,
            ">=456"
        );
    }

    @Test
    public void testGreaterThanEqualsString3() {
        this.testFalse(
            22,
            ">=456"
        );
    }

    @Test
    public void testGreaterThanEqualsString4() {
        this.testTrue(
            123,
            ">=100+20+3"
        );
    }

    @Test
    public void testGreaterThanEqualsString5() {
        this.testFalse(
            123,
            ">=100+20+4"
        );
    }

    // not equals......................................................................................................

    @Test
    public void testNotEqualsString() {
        this.testTrue(
            789,
            "<>456"
        );
    }

    @Test
    public void testNotEqualsString2() {
        this.testFalse(
            456,
            "<>456"
        );
    }

    @Test
    public void testNotEqualExpression() {
        this.testFalse(
            456,
            "<>400+50+6"
        );
    }

    @Test
    public void testNotEqualExpression2() {
        this.testTrue(
            456,
            "<>400+50+6+1"
        );
    }

    // wildcards.......................................................................................................

    @Test
    public void testWildcard() {
        this.testTrue(
            123.5,
            "1?3.5"
        );
    }

    @Test
    public void testWildcard2() {
        this.testTrue(
            1234.5,
            "1*5"
        );
    }

    @Test
    public void testWildcard3() {
        this.testFalse(
            123.5,
            "x*"
        );
    }

    private void testTrue(final Object value,
                          final Object condition) {
        this.testTrue(
            SpreadsheetExpressionFunctionNumberIfPredicate.with(
                condition,
                this.context()
            ),
            value
        );
    }

    private void testFalse(final Object value,
                           final Object condition) {
        this.testFalse(
            SpreadsheetExpressionFunctionNumberIfPredicate.with(
                condition,
                this.context()
            ),
            value
        );
    }

    private SpreadsheetExpressionEvaluationContext context() {
        final Locale locale = Locale.ENGLISH;

        final SpreadsheetMetadata metadata = SpreadsheetMetadata.EMPTY
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_ID, SpreadsheetId.parse("1234"))
            .set(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME, SpreadsheetName.with("Untitled5678"))
            .set(SpreadsheetMetadataPropertyName.LOCALE, locale)
            .loadFromLocale(
                LocaleContexts.jre(locale)
            ).set(
                SpreadsheetMetadataPropertyName.AUDIT_INFO,
                AuditInfo.with(
                    EmailAddress.parse("creator@example.com"),
                    LocalDateTime.of(1999, 12, 31, 12, 58, 59),
                    EmailAddress.parse("modified@example.com"),
                    LocalDateTime.of(2000, 1, 2, 3, 4, 5)
                )
            ).set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
            .set(SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
            .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
            .set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, ExpressionNumberKind.BIG_DECIMAL)
            .set(SpreadsheetMetadataPropertyName.FORMULA_CONVERTER, ConverterSelector.parse("collection(text, number, basic, spreadsheet-value)"))
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

                private final StorageStore storage = StorageStores.tree(STORAGE_CONTEXT);
            },
            SPREADSHEET_FORMULA_CONVERTER_CONTEXT,
            ENVIRONMENT_CONTEXT,
            (Optional<SpreadsheetCell> cell) -> {
                throw new UnsupportedOperationException();
            },
            FormHandlerContexts.fake(),
            TERMINAL_CONTEXT,
            EXPRESSION_FUNCTION_PROVIDER,
            PROVIDER_CONTEXT
        );
    }
}
