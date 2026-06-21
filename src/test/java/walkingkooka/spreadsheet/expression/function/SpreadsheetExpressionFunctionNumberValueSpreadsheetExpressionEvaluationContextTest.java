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

import walkingkooka.currency.CurrencyExchange;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.EnvironmentWatcher;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.SpreadsheetStrings;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.environment.SpreadsheetEnvironmentContext;
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContextTesting;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.store.SpreadsheetMetadataStores;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;
import walkingkooka.spreadsheet.validation.SpreadsheetValidatorContext;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.storage.StoragePath;
import walkingkooka.store.StoreWatcher;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContextObjectPostProcessor;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallUnmarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.validation.form.FormField;

import java.math.MathContext;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContextTest implements SpreadsheetExpressionEvaluationContextTesting<SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext>,
    SpreadsheetMetadataTesting {

    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    private final static String CURRENCY_SYMBOL = "AUD";
    private final static char DECIMAL_SEPARATOR = '/';
    private final static String EXPONENT_SYMBOL = "HELLO";
    private final static char GROUP_SEPARATOR = '/';
    private final static String INFINITY_SYMBOL = "Infinity!";
    private final static char MONETARY_DECIMAL_SEPARATOR = '*';
    private final static String NAN_SYMBOL = "Nan!";
    private final static char NEGATIVE_SYMBOL = 'N';
    private final static char PERCENT_SYMBOL = 'R';
    private final static char PERMILL_SYMBOL = '^';
    private final static char POSITIVE_SYMBOL = 'P';
    private final static char ZERO_DIGIT = '0';

    @Override
    public void testCurrencyForCurrencyCodeWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testLoadCellWithNullCellFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testLoadCellRangeWithNullRangeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testLoadLabelWithNullLabelFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testReferenceWithNullReferenceFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testExpressionFunctionWithNullFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEvaluateExpressionUnknownFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testFindByLocaleTextWithNullTextFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testFindByLocaleTextWithNegativeOffsetFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testFindByLocaleTextWithInvalidCountFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testIsPureNullNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testLocaleForLanguageTagWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testLocaleTextWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLocaleWithDifferent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMultiplyWithNullLeftFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMultiplyWithNullRightFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMultiplyWithNullTypeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseExpressionNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseValueOrExpressionNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testResolveLabelWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSpreadsheetFormatterContextWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRemoveEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetCurrencyWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetIndentationWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLineEndingWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLocaleWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetTimeOffsetWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetUserWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEvaluateWithEmptyStringReturnsError() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEvaluateWithWhitespaceStringReturnsError() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseExpressionWithEmptyStringFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseExpressionWithOnlyWhitespaceStringFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseExpressionWithOnlyWhitespaceStringFails2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseValueOrExpressionWithEmptyStringFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseValueOrExpressionWithOnlyWhitespaceStringFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseValueOrExpressionWithOnlyWhitespaceStringFails2() {
        throw new UnsupportedOperationException();
    }

    // DateTimeContextTesting2..........................................................................................

    @Override
    public void testAmpms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAmpmNegativeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAmpmInvalidFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNames2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNameNegativeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNameInvalidFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNamesAbbreviation2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNameAbbrevationNegativeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testMonthNameAbbreviationInvalidFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetObjectPostProcessor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetPreProcessor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTwoDigitYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNames2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNameNegativeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNameInvalidFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNameAbbreviations2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNameAbbrevationNegativeFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testWeekDayNameAbbreviationInvalidFails() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext createContext() {
        final SpreadsheetEnvironmentContext spreadsheetEnvironmentContext = SPREADSHEET_ENVIRONMENT_CONTEXT.cloneEnvironment();
        spreadsheetEnvironmentContext.setSpreadsheetId(
            Optional.of(
                SpreadsheetId.with(1)
            )
        );

        return SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext.with(
            DECIMAL_SEPARATOR,
            GROUP_SEPARATOR,
            new FakeSpreadsheetExpressionEvaluationContext() {

                @Override
                public Optional<SpreadsheetCell> cell() {
                    return Optional.empty();
                }

                @Override
                public String currencySymbol() {
                    return CURRENCY_SYMBOL;
                }

                @Override
                public int decimalNumberDigitCount() {
                    return DEFAULT_NUMBER_DIGIT_COUNT;
                }

                @Override
                public char decimalSeparator() {
                    return DECIMAL_SEPARATOR;
                }

                @Override
                public String exponentSymbol() {
                    return EXPONENT_SYMBOL;
                }

                @Override
                public char groupSeparator() {
                    return GROUP_SEPARATOR;
                }

                @Override
                public String infinitySymbol() {
                    return INFINITY_SYMBOL;
                }

                @Override
                public MathContext mathContext() {
                    return MATH_CONTEXT;
                }

                @Override
                public String nanSymbol() {
                    return NAN_SYMBOL;
                }

                @Override
                public char monetaryDecimalSeparator() {
                    return MONETARY_DECIMAL_SEPARATOR;
                }

                @Override
                public char negativeSign() {
                    return NEGATIVE_SYMBOL;
                }

                @Override
                public char percentSymbol() {
                    return PERCENT_SYMBOL;
                }

                @Override
                public char permillSymbol() {
                    return PERMILL_SYMBOL;
                }

                @Override
                public char positiveSign() {
                    return POSITIVE_SYMBOL;
                }

                @Override
                public char zeroDigit() {
                    return ZERO_DIGIT;
                }

                // FormHandlerContext...................................................................................

                @Override
                public Optional<Object> loadFormFieldValue(final SpreadsheetValidationReference reference) {
                    Objects.requireNonNull(reference, "reference");

                    throw new UnsupportedOperationException();
                }

                @Override
                public SpreadsheetDelta saveFormFieldValues(final List<FormField<SpreadsheetValidationReference>> fields) {
                    Objects.requireNonNull(fields, "fields");

                    throw new UnsupportedOperationException();
                }

                @Override
                public SpreadsheetValidatorContext validatorContext(final SpreadsheetValidationReference reference) {
                    Objects.requireNonNull(reference, "reference");

                    throw new UnsupportedOperationException();
                }

                @Override
                public Optional<Number> currencyExchangeRate(final CurrencyExchange currencyExchange,
                                                             final Optional<LocalDateTime> dateTime) {
                    Objects.requireNonNull(currencyExchange, "currencyExchange");
                    Objects.requireNonNull(dateTime, "dateTime");

                    throw new UnsupportedOperationException();
                }

                @Override
                public Optional<Currency> currencyForLocale(final Locale locale) {
                    Objects.requireNonNull(locale, "locale");

                    throw new UnsupportedOperationException();
                }

                @Override
                public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
                    return this.localeContext.dateTimeSymbolsForLocale(locale);
                }

                @Override
                public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
                    return this.localeContext.decimalNumberSymbolsForLocale(locale);
                }

                @Override
                public CaseSensitivity stringEqualsCaseSensitivity() {
                    return SpreadsheetStrings.CASE_SENSITIVITY;
                }

                @Override
                public StoragePath parseStoragePath(final String text) {
                    return StoragePath.parseSpecial(
                        text,
                        this // HasUserDirectories
                    );
                }

                // EnvironmentContext...................................................................................

                @Override
                public SpreadsheetExpressionEvaluationContext setEnvironmentContext(final EnvironmentContext environmentContext) {
                    Objects.requireNonNull(environmentContext, "environmentContext");
                    return SpreadsheetExpressionEvaluationContexts.fake();
                }

                @Override
                public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
                    return spreadsheetEnvironmentContext.environmentValue(name);
                }

                @Override
                public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
                    spreadsheetEnvironmentContext.removeEnvironmentValue(name);
                }

                @Override
                public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                                    final T value) {
                    spreadsheetEnvironmentContext.setEnvironmentValue(
                        name,
                        value
                    );
                }

                @Override
                public Charset charset() {
                    return StandardCharsets.UTF_8;
                }

                @Override
                public Optional<EmailAddress> user() {
                    return spreadsheetEnvironmentContext.user();
                }

                @Override
                public void setUser(final Optional<EmailAddress> user) {
                    spreadsheetEnvironmentContext.setUser(user);
                }

                @Override
                public Indentation indentation() {
                    return spreadsheetEnvironmentContext.indentation();
                }

                @Override
                public void setIndentation(final Indentation indentation) {
                    spreadsheetEnvironmentContext.setIndentation(indentation);
                }
                
                @Override
                public LineEnding lineEnding() {
                    return spreadsheetEnvironmentContext.lineEnding();
                }

                @Override
                public void setLineEnding(final LineEnding lineEnding) {
                    spreadsheetEnvironmentContext.setLineEnding(lineEnding);
                }
                
                @Override
                public Locale locale() {
                    return spreadsheetEnvironmentContext.locale();
                }

                @Override
                public void setLocale(final Locale locale) {
                    spreadsheetEnvironmentContext.setLocale(locale);
                }

                @Override
                public LocalDateTime now() {
                    return spreadsheetEnvironmentContext.now();
                }

                @Override
                public AbsoluteUrl serverUrl() {
                    return spreadsheetEnvironmentContext.serverUrl();
                }

                @Override
                public Optional<SpreadsheetId> spreadsheetId() {
                    return spreadsheetEnvironmentContext.spreadsheetId();
                }

                @Override
                public void setSpreadsheetId(final Optional<SpreadsheetId> spreadsheetId) {
                    spreadsheetEnvironmentContext.setSpreadsheetId(spreadsheetId);
                }

                @Override
                public Runnable addEnvironmentWatcher(final EnvironmentWatcher watcher) {
                    Objects.requireNonNull(watcher, "watcher");
                    throw new UnsupportedOperationException();
                }

                @Override
                public Runnable addEnvironmentWatcherOnce(final EnvironmentWatcher watcher) {
                    Objects.requireNonNull(watcher, "watcher");
                    throw new UnsupportedOperationException();
                }

                private final LocaleContext localeContext = LocaleContexts.jre(Locale.ENGLISH);

                @Override
                public JsonNode marshall(final Object value) {
                    return this.jsonNodeMarshallUnmarshallContext.marshall(value);
                }

                @Override
                public JsonNode marshallEnumSet(final Set<? extends Enum<?>> enumSet) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallEnumSet(enumSet);
                }

                @Override
                public JsonNode marshallOptional(final Optional<?> optional) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallOptional(optional);
                }

                @Override
                public JsonNode marshallOptionalWithType(final Optional<?> optional) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallOptionalWithType(optional);
                }

                @Override
                public JsonNode marshallWithType(final Object value) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallWithType(value);
                }

                @Override
                public JsonNode marshallCollection(final Collection<?> collection) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallCollection(collection);
                }

                @Override
                public JsonNode marshallMap(final Map<?, ?> map) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallMap(map);
                }

                @Override
                public JsonNode marshallCollectionWithType(final Collection<?> collection) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallCollectionWithType(collection);
                }

                @Override
                public JsonNode marshallMapWithType(final Map<?, ?> map) {
                    return this.jsonNodeMarshallUnmarshallContext.marshallMapWithType(map);
                }

                @Override
                public SpreadsheetExpressionEvaluationContext setObjectPostProcessor(final JsonNodeMarshallContextObjectPostProcessor processor) {
                    Objects.requireNonNull(processor, "processor");
                    return this;
                }

                @Override
                public SpreadsheetExpressionEvaluationContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
                    Objects.requireNonNull(processor, "processor");
                    return this;
                }

                @Override
                public Optional<Class<?>> registeredType(final JsonString string) {
                    return this.jsonNodeMarshallUnmarshallContext.registeredType(string);
                }

                @Override
                public Optional<JsonString> typeName(final Class<?> type) {
                    return this.jsonNodeMarshallUnmarshallContext.typeName(type);
                }

                @Override
                public <T> T unmarshall(final JsonNode node,
                                        final Class<T> type) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshall(
                        node,
                        type
                    );
                }

                @Override
                public <T extends Enum<T>> Set<T> unmarshallEnumSet(final JsonNode node,
                                                                    final Class<T> enumClass,
                                                                    final Function<String, T> stringToEnum) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallEnumSet(
                        node,
                        enumClass,
                        stringToEnum
                    );
                }

                @Override
                public <T> Optional<T> unmarshallOptional(final JsonNode node,
                                                          final Class<T> type) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallOptional(
                        node,
                        type
                    );
                }

                @Override
                public <T> Optional<T> unmarshallOptionalWithType(final JsonNode node) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallOptionalWithType(node);
                }

                @Override
                public <T> List<T> unmarshallList(final JsonNode node,
                                                  final Class<T> elementType) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallList(
                        node,
                        elementType
                    );
                }

                @Override
                public <T> Set<T> unmarshallSet(final JsonNode node,
                                                final Class<T> elementType) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallSet(
                        node,
                        elementType
                    );
                }

                @Override
                public <K, V> Map<K, V> unmarshallMap(final JsonNode node,
                                                      final Class<K> keyType,
                                                      final Class<V> valueType) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallMap(
                        node,
                        keyType,
                        valueType
                    );
                }

                @Override
                public <T> T unmarshallWithType(final JsonNode node) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallWithType(node);
                }

                @Override
                public <T> List<T> unmarshallListWithType(final JsonNode node) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallListWithType(node);
                }

                @Override
                public <T> Set<T> unmarshallSetWithType(final JsonNode node) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallSetWithType(node);
                }

                @Override
                public <K, V> Map<K, V> unmarshallMapWithType(final JsonNode node) {
                    return this.jsonNodeMarshallUnmarshallContext.unmarshallMapWithType(node);
                }

                private final JsonNodeMarshallUnmarshallContext jsonNodeMarshallUnmarshallContext = JsonNodeMarshallUnmarshallContexts.basic(
                    JsonNodeMarshallContexts.basic(),
                    JsonNodeUnmarshallContexts.basic(
                        ExpressionNumberKind.DEFAULT,
                        this, // CurrencyCodeLanguageTagContext
                        this.mathContext()
                    )
                );

                // SpreadsheetMetadataContext...................................................................................

                @Override
                public SpreadsheetMetadata createMetadata(final EmailAddress user,
                                                          final Optional<Locale> locale) {
                    return this.spreadsheetMetadataContext.createMetadata(
                        user,
                        locale
                    );
                }

                @Override
                public Optional<SpreadsheetMetadata> loadMetadata(final SpreadsheetId id) {
                    return this.spreadsheetMetadataContext.loadMetadata(id);
                }

                @Override
                public SpreadsheetMetadata saveMetadata(final SpreadsheetMetadata metadata) {
                    return this.spreadsheetMetadataContext.saveMetadata(metadata);
                }

                @Override
                public void deleteMetadata(final SpreadsheetId id) {
                    this.spreadsheetMetadataContext.deleteMetadata(id);
                }

                @Override
                public Runnable addMetadataWatcher(final StoreWatcher<SpreadsheetMetadata> watcher) {
                    return this.spreadsheetMetadataContext.addMetadataWatcher(watcher);
                }

                @Override
                public Runnable addMetadataWatcherOnce(final StoreWatcher<SpreadsheetMetadata> watcher) {
                    return this.spreadsheetMetadataContext.addMetadataWatcherOnce(watcher);
                }

                @Override
                public List<SpreadsheetMetadata> findMetadataBySpreadsheetName(final String name,
                                                                               final int offset,
                                                                               final int count) {
                    return this.spreadsheetMetadataContext.findMetadataBySpreadsheetName(
                        name,
                        offset,
                        count
                    );
                }

                private final SpreadsheetMetadataContext spreadsheetMetadataContext = SpreadsheetMetadataContexts.basic(
                    (e, l) -> SpreadsheetMetadataTesting.METADATA_EN_AU,
                    SpreadsheetMetadataStores.treeMap()
                );
            }
        );
    }

    @Override
    public String currencySymbol() {
        return CURRENCY_SYMBOL;
    }

    @Override
    public char decimalSeparator() {
        return DECIMAL_SEPARATOR;
    }

    @Override
    public int decimalNumberDigitCount() {
        return DecimalNumberContext.DEFAULT_NUMBER_DIGIT_COUNT;
    }

    @Override
    public String exponentSymbol() {
        return EXPONENT_SYMBOL;
    }

    @Override
    public char groupSeparator() {
        return GROUP_SEPARATOR;
    }

    @Override
    public String infinitySymbol() {
        return INFINITY_SYMBOL;
    }

    @Override
    public MathContext mathContext() {
        return MATH_CONTEXT;
    }

    @Override
    public char monetaryDecimalSeparator() {
        return MONETARY_DECIMAL_SEPARATOR;
    }

    @Override
    public String nanSymbol() {
        return NAN_SYMBOL;
    }

    @Override
    public char negativeSign() {
        return NEGATIVE_SYMBOL;
    }

    @Override
    public char percentSymbol() {
        return PERCENT_SYMBOL;
    }

    @Override
    public char permillSymbol() {
        return PERMILL_SYMBOL;
    }

    @Override
    public char positiveSign() {
        return POSITIVE_SYMBOL;
    }

    @Override
    public char zeroDigit() {
        return ZERO_DIGIT;
    }

    @Override
    public void testSetSpreadsheetMetadataWithNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetSpreadsheetMetadataWithDifferentIdFails() {
        throw new UnsupportedOperationException();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext> type() {
        return SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext.class;
    }
}
