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

import walkingkooka.Either;
import walkingkooka.convert.Converter;
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContextDelegator;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextDelegator;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContexts;
import walkingkooka.spreadsheet.formula.parser.SpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.validation.SpreadsheetValidatorContext;
import walkingkooka.storage.StorageStore;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextDelegator;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContextPreProcessor;
import walkingkooka.validation.form.Form;
import walkingkooka.validation.form.FormField;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link SpreadsheetExpressionEvaluationContext} that has a main goal of preparing parameters with a converter
 * that uses the provided decimal separator and group separator.
 */
final class SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext implements SpreadsheetExpressionEvaluationContext,
        DateTimeContextDelegator,
        EnvironmentContextDelegator,
        JsonNodeMarshallContextDelegator,
        JsonNodeUnmarshallContextDelegator {

    static SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext with(final char decimalSeparator,
                                                                                               final char groupSeparator,
                                                                                               final SpreadsheetExpressionEvaluationContext context) {
        return new SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext(
                decimalSeparator,
                groupSeparator,
                context
        );
    }

    private SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext(final char decimalSeparator,
                                                                                           final char groupSeparator,
                                                                                           final SpreadsheetExpressionEvaluationContext context) {
        super();

        this.decimalSeparator = decimalSeparator;
        this.groupSeparator = groupSeparator;
        this.context = context;
    }

    @Override
    public Optional<SpreadsheetCell> cell() {
        return this.context.cell();
    }

    @Override
    public SpreadsheetExpressionEvaluationContext setCell(final Optional<SpreadsheetCell> cell) {
        return SpreadsheetExpressionEvaluationContexts.cell(
                cell,
                this
        );
    }

    @Override
    public Optional<SpreadsheetCell> loadCell(final SpreadsheetCellReference reference) {
        return this.context.loadCell(reference);
    }

    @Override
    public Set<SpreadsheetCell> loadCellRange(final SpreadsheetCellRangeReference range) {
        return this.context.loadCellRange(range);
    }

    @Override
    public Optional<SpreadsheetLabelMapping> loadLabel(final SpreadsheetLabelName labelName) {
        return this.context.loadLabel(labelName);
    }

    @Override
    public Optional<SpreadsheetColumnReference> nextEmptyColumn(final SpreadsheetRowReference row) {
        return this.context.nextEmptyColumn(row);
    }

    @Override
    public Optional<SpreadsheetRowReference> nextEmptyRow(final SpreadsheetColumnReference column) {
        return this.context.nextEmptyRow(column);
    }

    @Override
    public SpreadsheetFormulaParserToken parseFormula(final TextCursor expression) {
        return this.context.parseFormula(expression);
    }

    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName labelName) {
        return this.context.resolveLabel(labelName);
    }

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public void setSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.context.setSpreadsheetMetadata(metadata);
    }

    @Override
    public SpreadsheetExpressionEvaluationContext setPreProcessor(final JsonNodeUnmarshallContextPreProcessor processor) {
        final SpreadsheetExpressionEvaluationContext before = this.context;
        final SpreadsheetExpressionEvaluationContext after = before.setPreProcessor(processor);

        return before.equals(after) ?
                this :
                new SpreadsheetExpressionFunctionNumberValueSpreadsheetExpressionEvaluationContext(
                        this.decimalSeparator,
                        this.groupSeparator,
                        after
                );
    }

    @Override
    public AbsoluteUrl serverUrl() {
        return this.context.serverUrl();
    }

    @Override
    public Optional<Object> validationValue() {
        return this.context.validationValue();
    }

    @Override
    public CaseSensitivity stringEqualsCaseSensitivity() {
        return this.context.stringEqualsCaseSensitivity();
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public Converter<SpreadsheetConverterContext> converter() {
        return this.context.converter();
    }

    @Override
    public Object evaluateExpression(final Expression expression) {
        return this.context.evaluateExpression(expression);
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        return this.context.expressionFunction(name);
    }

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        return this.context.prepareParameter(parameter, value);
    }

    @Override
    public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                   final List<Object> parameters) {
        return this.context.evaluateFunction(
                function,
                parameters
        );
    }

    @Override
    public Object handleException(final RuntimeException cause) {
        return this.context.handleException(cause);
    }

    @Override
    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
        return this.context.reference(reference);
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type) {
        return this.converter()
                .canConvert(
                        value,
                        type,
                        this
                );
    }

    @Override
    public <T> Either<T, String> convert(final Object value,
                                         final Class<T> type) {
        return this.converter()
                .convert(
                        value,
                        type,
                        this
                );
    }

    @Override
    public long dateOffset() {
        return this.context.dateOffset();
    }

    @Override
    public String currencySymbol() {
        return this.context.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.decimalSeparator;
    }

    private final char decimalSeparator;

    @Override
    public String exponentSymbol() {
        return this.context.exponentSymbol();
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    @Override
    public char groupSeparator() {
        return this.groupSeparator;
    }

    private final char groupSeparator;

    @Override
    public String infinitySymbol() {
        return this.context.infinitySymbol();
    }

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public char monetaryDecimalSeparator() {
        return this.context.monetaryDecimalSeparator();
    }

    @Override
    public String nanSymbol() {
        return this.context.nanSymbol();
    }

    @Override
    public char negativeSign() {
        return this.context.negativeSign();
    }

    @Override
    public char percentSymbol() {
        return this.context.percentSymbol();
    }

    @Override
    public char permillSymbol() {
        return this.context.permillSymbol();
    }

    @Override
    public char positiveSign() {
        return this.context.positiveSign();
    }

    @Override
    public char zeroDigit() {
        return this.context.zeroDigit();
    }

    @Override
    public DecimalNumberSymbols decimalNumberSymbols() {
        return this.context.decimalNumberSymbols();
    }

    @Override
    public boolean isPure(final ExpressionFunctionName name) {
        return this.context.isPure(name);
    }

    @Override
    public StorageStore storage() {
        return this.context.storage();
    }

    @Override
    public SpreadsheetExpressionReference validationReference() {
        return this.context.validationReference();
    }

    private final SpreadsheetExpressionEvaluationContext context;

    // EnvironmentContextDelegator......................................................................................

    @Override
    public EnvironmentContext environmentContext() {
        return this.context;
    }

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    // FormHandlerContext...............................................................................................

    @Override
    public Form<SpreadsheetExpressionReference> form() {
        return this.context.form();
    }

    @Override
    public Comparator<SpreadsheetExpressionReference> formFieldReferenceComparator() {
        return this.context.formFieldReferenceComparator();
    }

    @Override
    public Optional<Object> loadFormFieldValue(final SpreadsheetExpressionReference reference) {
        return this.context.loadFormFieldValue(reference);
    }

    @Override
    public SpreadsheetDelta saveFormFieldValues(final List<FormField<SpreadsheetExpressionReference>> fields) {
        return this.context.saveFormFieldValues(fields);
    }

    @Override
    public SpreadsheetValidatorContext validatorContext(final SpreadsheetExpressionReference reference) {
        return this.context.validatorContext(reference);
    }

    // DateTimeContextDelegator.........................................................................................

    @Override
    public DateTimeContext dateTimeContext() {
        return this.context;
    }

    // JsonNodeMarshallContextDelegator.................................................................................

    @Override
    public JsonNodeMarshallContext jsonNodeMarshallContext() {
        return this.context;
    }

    // JsonNodeUnmarshallContextDelegator...............................................................................

    @Override
    public JsonNodeUnmarshallContext jsonNodeUnmarshallContext() {
        return this.context;
    }

    @Override
    public JsonNodeContext jsonNodeContext() {
        return this.context;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
