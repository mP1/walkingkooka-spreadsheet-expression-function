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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.template.SpreadsheetTemplateContexts;
import walkingkooka.template.TemplateValueName;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterCardinality;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that accepts a single parameter holding the template. Following the template are pairs with the
 * first being the {@link TemplateValueName} and the other being the value. The template may include label references
 * and function expressions.
 */
final class SpreadsheetExpressionFunctionStringTemplate extends SpreadsheetExpressionFunctionString {

    /**
     * Singleton
     */
    final static SpreadsheetExpressionFunctionStringTemplate INSTANCE = new SpreadsheetExpressionFunctionStringTemplate();

    private SpreadsheetExpressionFunctionStringTemplate() {
        super("template");
    }

    /**
     * A {@link String} or text that holds the template.
     */
    final static ExpressionFunctionParameter<String> TEMPLATE = ExpressionFunctionParameter.with(
            ExpressionFunctionParameterName.with("template"),
            String.class,
            ExpressionFunctionParameter.NO_TYPE_PARAMETERS,
            ExpressionFunctionParameterCardinality.REQUIRED,
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
    );

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        final List<ExpressionFunctionParameter<?>> parameters = Lists.array();

        parameters.add(TEMPLATE);

        int parameterCounter = 1;
        int missingParameterValue = -1;

        for (int i = 1; i < count; i++) {
            if (-1 == missingParameterValue) {
                parameters.add(
                        parameterName(parameterCounter)
                );
                missingParameterValue = parameterCounter;
            } else {
                parameters.add(
                        parameterValue(parameterCounter)
                );
                parameterCounter++;
                missingParameterValue = -1;
            }
        }

        if (-1 != missingParameterValue) {
            throw new IllegalArgumentException("Named parameter " + missingParameterValue + ": missing value");
        }

        return parameters;
    }

    private static ExpressionFunctionParameter<TemplateValueName> parameterName(final int parameter) {
        return TEMPLATE_VALUE_NAME_PARAMETER.setName(
                ExpressionFunctionParameterName.with("templateValueName" + parameter)
        );
    }

    /**
     * The name will be replaced when the parameter list is built.
     */
    final static ExpressionFunctionParameter<TemplateValueName> TEMPLATE_VALUE_NAME_PARAMETER = ExpressionFunctionParameter.with(
            ExpressionFunctionParameterName.with("templateValueName1"),
            TemplateValueName.class,
            ExpressionFunctionParameter.NO_TYPE_PARAMETERS,
            ExpressionFunctionParameterCardinality.REQUIRED,
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
    );

    private static ExpressionFunctionParameter<Object> parameterValue(final int parameter) {
        return TEMPLATE_VALUE_PARAMETER.setName(
                ExpressionFunctionParameterName.with("templateValue" + parameter)
        );
    }

    /**
     * The name will be replaced when the parameter list is built.
     */
    final static ExpressionFunctionParameter<Object> TEMPLATE_VALUE_PARAMETER = ExpressionFunctionParameter.with(
            ExpressionFunctionParameterName.with("templateValue1"),
            Object.class,
            ExpressionFunctionParameter.NO_TYPE_PARAMETERS,
            ExpressionFunctionParameterCardinality.REQUIRED,
            ExpressionFunctionParameterKind.CONVERT_EVALUATE
    );

    @Override
    public String apply(final List<Object> parameters,
                        final SpreadsheetExpressionEvaluationContext context) {
        this.checkParameterCount(parameters);

        final String template = TEMPLATE.getOrFail(parameters, 0);

        // TODO would be good to be able to pass templates as values.
        return SpreadsheetTemplateContexts.spreadsheet(
                context.spreadsheetMetadata()
                        .spreadsheetParserContext(
                                context.cell(),
                                context::now
                        ), // SpreadsheetParserContext
                context, // SpreadsheetExpressionEvaluationContext
                (final TemplateValueName name) -> {
                    Expression value = null;

                    // $name might be a parameter to this function search that first.
                    for (int i = 1; i < parameters.size(); i = i + 2) {
                        final TemplateValueName parameterName = TEMPLATE_VALUE_NAME_PARAMETER.getOrFail(parameters, i);

                        if (name.equals(parameterName)) {
                            value = Expression.value(
                                    TEMPLATE_VALUE_PARAMETER.getOrFail(parameters, i + 1)
                            );
                            break;
                        }
                    }

                    // not a named parameter, try as a label
                    if (null == value) {
                        value = Expression.reference(
                                SpreadsheetSelection.labelName(name.text())
                        );
                    }

                    return value; // Function<TemplateValueName, Expression> templateValueNameToExpression
                }
        ).parseTemplateAndRenderToString(
                template,
                LineEnding.NL // TODO parameterise, maybe store in SpreadsheetMetadata.DEFAULT_LINE_ENDING
        );
    }
}
