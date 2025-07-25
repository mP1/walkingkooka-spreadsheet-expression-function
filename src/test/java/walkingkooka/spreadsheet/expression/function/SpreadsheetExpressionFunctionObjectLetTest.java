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
import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContexts;
import walkingkooka.spreadsheet.convert.SpreadsheetConvertersConverterProviders;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReferenceLoaders;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.store.repo.FakeSpreadsheetStoreRepository;
import walkingkooka.storage.StorageStore;
import walkingkooka.storage.StorageStores;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.validation.form.FormHandlerContexts;

import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionObjectLetTest extends SpreadsheetExpressionFunctionObjectTestCase<SpreadsheetExpressionFunctionObjectLet> {

    @Test
    public void testZeroParametersFails() {
        this.applyFails2(
            Lists.empty(),
            "Missing last parameter with expression"
        );
    }

    @Test
    public void testEvenNumberParameterCountFails() {
        this.applyFails2(
            Lists.of(1, 2),
            "Missing last parameter with expression"
        );
    }

    @Test
    public void testInvalidVariableNameFails() {
        this.applyFails2(
            Lists.of(
                "!Label",
                2,
                3
            ),
            null //"Invalid character '!' at 0 in \"!Label\""
        );
    }

    @Test
    public void testDuplicateLabelFails() {
        this.applyFails2(
            Lists.of(
                SpreadsheetSelection.labelName("Duplicate1"),
                1,
                SpreadsheetSelection.labelName("DUPLICATE1"),
                2,
                999
            ),
            "Duplicate name \"DUPLICATE1\" in value 3"
        );
    }

    @Test
    public void testDuplicateLabelFails2() {
        this.applyFails2(
            Lists.of(
                SpreadsheetSelection.labelName("Duplicate1"),
                1,
                SpreadsheetSelection.labelName("X"),
                2,
                SpreadsheetSelection.labelName("DUPLICATE1"),
                3,
                999
            ),
            "Duplicate name \"DUPLICATE1\" in value 5"
        );
    }

    private void applyFails2(final List<Object> parameters,
                             final String message) {
        final SpreadsheetExpressionFunctionObjectLet function = this.createBiFunction();
        final SpreadsheetExpressionEvaluationContext context = this.createContext();

        final RuntimeException thrown = assertThrows(
            RuntimeException.class, () -> function.apply(
                context.prepareParameters(
                    function,
                    parameters
                ),
                context
            )
        );
        if (null != message) {
            this.checkEquals(
                message,
                thrown.getMessage(),
                "message"
            );
        }
    }

    @Test
    public void testOnlyComputedValueNumberLiteral() {
        this.applyAndCheck2(
            Lists.of(
                123
            ),
            EXPRESSION_NUMBER_KIND.create(123)
        );
    }

    @Test
    public void testOnlyComputedValueStringLiteral() {
        this.applyAndCheck2(
            Lists.of(
                "ABC"
            ),
            "ABC"
        );
    }

    @Test
    public void testNamedValueIgnored() {
        this.applyAndCheck3(
            Lists.of(
                "ABC",
                123,
                "DEF"
            ),
            "DEF"
        );
    }

    @Test
    public void testComputedValueReferencesNamedValue() {
        final String name = "x";
        final int value = 23;

        this.applyAndCheck3(
            Lists.of(
                name,
                value,
                Expression.add(
                    Expression.reference(
                        SpreadsheetSelection.labelName(name)
                    ),
                    Expression.value(100)
                )
            ),
            EXPRESSION_NUMBER_KIND.create(100 + 23)
        );
    }

    @Test
    public void testComputedValueReferencesNamedValue2() {
        final String name1 = "x";
        final ExpressionNumber value1 = EXPRESSION_NUMBER_KIND.one();

        final String name2 = "y";
        final ExpressionNumber value2 = EXPRESSION_NUMBER_KIND.create(20);

        this.applyAndCheck3(
            Lists.of(
                name1,
                value1,
                name2,
                value2,
                Expression.add(
                    Expression.reference(
                        SpreadsheetSelection.labelName(name1)
                    ),
                    Expression.reference(
                        SpreadsheetSelection.labelName(name2)
                    )
                )
            ),
            EXPRESSION_NUMBER_KIND.create(20 + 1)
        );
    }

    @Test
    public void testComputedValueReferencesNamedValue3() {
        final String name1 = "x";
        final ExpressionNumber value1 = EXPRESSION_NUMBER_KIND.one();

        final String name2 = "y";
        final ExpressionNumber value2 = EXPRESSION_NUMBER_KIND.create(20);

        final String name3 = "z";
        final ExpressionNumber value3 = EXPRESSION_NUMBER_KIND.create(300);

        this.applyAndCheck3(
            Lists.of(
                name1,
                value1,
                name2,
                value2,
                name3,
                value3,
                Expression.add(
                    Expression.reference(
                        SpreadsheetSelection.labelName(name1)
                    ),
                    Expression.add(
                        Expression.reference(
                            SpreadsheetSelection.labelName(name2)
                        ),
                        Expression.reference(
                            SpreadsheetSelection.labelName(name3)
                        )
                    )
                )
            ),
            EXPRESSION_NUMBER_KIND.create(300 + 20 + 1)
        );
    }

    private void applyAndCheck3(final List<Object> parameters,
                                final Object expected) {
        this.applyAndCheck3(
            parameters,
            this.createContext(),
            expected
        );
    }

    private void applyAndCheck3(final List<Object> parameters,
                                final SpreadsheetExpressionEvaluationContext context,
                                final Object expected) {
        this.applyAndCheck3(
            this.createBiFunction(),
            parameters,
            context,
            expected
        );
    }

    private void applyAndCheck3(final SpreadsheetExpressionFunctionObjectLet function,
                                final List<Object> parameters,
                                final SpreadsheetExpressionEvaluationContext context,
                                final Object expected) {
        assertEquals(
            expected,
            function.apply(
                context.prepareParameters(function, parameters),
                context
            ),
            () -> "Wrong result for " + function + " for params: " + CharSequences.quoteIfChars(parameters) + "," + context);
    }


    // test related factories...........................................................................................

    @Override
    public SpreadsheetExpressionFunctionObjectLet createBiFunction() {
        return SpreadsheetExpressionFunctionObjectLet.INSTANCE;
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
                AuditInfo.with(
                    EmailAddress.parse("creator@example.com"),
                    LocalDateTime.of(1999, 12, 31, 12, 58, 59),
                    EmailAddress.parse("modified@example.com"),
                    LocalDateTime.of(2000, 1, 2, 3, 4, 5)
                )
            ).set(SpreadsheetMetadataPropertyName.CELL_CHARACTER_WIDTH, 1)
            .set(SpreadsheetMetadataPropertyName.DATE_TIME_OFFSET, Converters.EXCEL_1904_DATE_SYSTEM_OFFSET)
            .set(SpreadsheetMetadataPropertyName.DEFAULT_YEAR, 20)
            .set(SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND, EXPRESSION_NUMBER_KIND)
            .set(
                SpreadsheetMetadataPropertyName.FORMULA_CONVERTER,
                ConverterSelector.parse("collection (text-to-selection, selection-to-selection, selection-to-text, general)")
            ).set(SpreadsheetMetadataPropertyName.PRECISION, MathContext.DECIMAL32.getPrecision())
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
            metadata.spreadsheetConverterContext(
                SpreadsheetMetadata.NO_CELL,
                SpreadsheetConverterContexts.NO_VALIDATION_REFERENCE,
                SpreadsheetMetadataPropertyName.FORMULA_CONVERTER,
                SPREADSHEET_LABEL_NAME_RESOLVER,
                SpreadsheetConvertersConverterProviders.spreadsheetConverters(
                    (ProviderContext p) -> metadata.generalConverter(
                        SPREADSHEET_FORMATTER_PROVIDER,
                        SPREADSHEET_PARSER_PROVIDER,
                        p
                    )
                ),
                LOCALE_CONTEXT,
                PROVIDER_CONTEXT
            ),
            (Optional<SpreadsheetCell> cell) -> {
                throw new UnsupportedOperationException();
            },
            FormHandlerContexts.fake(),
            EXPRESSION_FUNCTION_PROVIDER,
            LOCALE_CONTEXT,
            PROVIDER_CONTEXT
        );
    }

    @Override
    public int minimumParameterCount() {
        return 3;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionObjectLet> type() {
        return SpreadsheetExpressionFunctionObjectLet.class;
    }
}
