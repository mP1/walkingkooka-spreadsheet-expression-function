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

package walkingkooka.spreadsheet.expression.function.provider;

import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.function.SpreadsheetExpressionFunctions;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProviders;

/**
 * Provider getter.
 */
public final class SpreadsheetExpressionFunctionProviders implements PublicStaticHelper {

    public final static ExpressionFunctionAliasSet FIND = expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY)
        .expressionFunctionInfos()
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterColor)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterFormatting)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterMetadata)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterStyle)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterTerminal)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterValidation)
        .aliasSet();

    public final static ExpressionFunctionAliasSet FORMATTING = expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY)
        .expressionFunctionInfos()
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterMetadata)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterTerminal)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterValidation)
        .aliasSet();

    public final static ExpressionFunctionAliasSet FORMULA = expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY)
        .expressionFunctionInfos()
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterColor)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterFormatting)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterMetadata)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterStyle)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterTerminal)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterValidation)
        .aliasSet();

    public final static ExpressionFunctionAliasSet TERMINAL = expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY)
        .expressionFunctionInfos()
        .aliasSet();

    public final static ExpressionFunctionAliasSet VALIDATION = expressionFunctionProvider(walkingkooka.spreadsheet.expression.SpreadsheetExpressionFunctions.NAME_CASE_SENSITIVITY)
        .expressionFunctionInfos()
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterColor)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterFormatting)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterMetadata)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterStyle)
        .deleteIf(SpreadsheetExpressionFunctionProviders::filterTerminal)
        .aliasSet();

    private static boolean filterCell(final ExpressionFunctionInfo info) {
        final String name = info.name()
            .value()
            .toLowerCase();
        return name.startsWith("cell") ||
            name.equals("cell");
    }

    private static boolean filterColor(final ExpressionFunctionInfo info) {
        final String name = info.name()
            .value()
            .toLowerCase();
        return name.contains("color") ||
            name.contains("rgb") ||
            name.equals("setalpha") ||
            name.equals("setblue") ||
            name.equals("setgreen") ||
            name.equals("setred") ||
            name.equals("getalpha") ||
            name.equals("getblue") ||
            name.equals("getgreen") ||
            name.equals("getred") ||
            name.equals("togray");
    }

    private static boolean filterFormatting(final ExpressionFunctionInfo info) {
        final String name = info.name()
            .value()
            .toLowerCase();
        return name.equals("hyperlink") ||
            name.equals("image");
    }

    private static boolean filterMetadata(final ExpressionFunctionInfo info) {
        final String name = info.name()
            .value()
            .toLowerCase();
        return name.contains("metadata");
    }

    private static boolean filterStyle(final ExpressionFunctionInfo info) {
        final String name = info.name()
            .value()
            .toLowerCase();
        return name.contains("style");
    }

    private static boolean filterTerminal(final ExpressionFunctionInfo info) {
        final String name = info.name()
            .value()
            .toLowerCase();
        return name.equals("print") ||
            name.equals("println") ||
            name.equals("readLine") ||
            name.equals("getEnv");
    }

    private static boolean filterValidation(final ExpressionFunctionInfo info) {
        final String name = info.name()
            .value()
            .toLowerCase();
        return name.startsWith("nextempty");
    }

    /**
     * An {@link ExpressionFunctionProvider} with all the functions in this project.
     */
    public static ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> expressionFunctionProvider(final CaseSensitivity nameCaseSensitivity) {
        final ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> expressionFunctionProvider;

        if (nameCaseSensitivity == CaseSensitivity.SENSITIVE) {
            if (null == caseSensitive) {
                caseSensitive = create(CaseSensitivity.SENSITIVE);
            }
            expressionFunctionProvider = caseSensitive;
        } else {
            if (null == caseInsensitive) {
                caseInsensitive = create(CaseSensitivity.INSENSITIVE);
            }
            expressionFunctionProvider = caseInsensitive;
        }

        return expressionFunctionProvider;
    }

    private static ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> caseSensitive;

    private static ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> caseInsensitive;

    /**
     * Factory that is called twice lazily once for each {@link CaseSensitivity}.
     */
    private static ExpressionFunctionProvider<SpreadsheetExpressionEvaluationContext> create(final CaseSensitivity nameCaseSensitivity) {
        return ExpressionFunctionProviders.basic(
            Url.parseAbsolute("https://github.com/mP1/walkingkooka-spreadsheet-expression-function/"),
            nameCaseSensitivity,
            Cast.to(
                Sets.of(
                    SpreadsheetExpressionFunctions.abs(),
                    SpreadsheetExpressionFunctions.acos(),
                    SpreadsheetExpressionFunctions.address(),
                    SpreadsheetExpressionFunctions.and(),
                    SpreadsheetExpressionFunctions.asin(),
                    SpreadsheetExpressionFunctions.atan(),
                    SpreadsheetExpressionFunctions.average(),
                    SpreadsheetExpressionFunctions.averageIf(),
                    SpreadsheetExpressionFunctions.badge(),
                    SpreadsheetExpressionFunctions.base(),
                    SpreadsheetExpressionFunctions.bin2dec(),
                    SpreadsheetExpressionFunctions.bin2hex(),
                    SpreadsheetExpressionFunctions.bin2oct(),
                    SpreadsheetExpressionFunctions.bitAnd(),
                    SpreadsheetExpressionFunctions.bitOr(),
                    SpreadsheetExpressionFunctions.bitXor(),
                    SpreadsheetExpressionFunctions.ceil(),
                    SpreadsheetExpressionFunctions.cell(),
                    SpreadsheetExpressionFunctions.cellDateTimeSymbols(),
                    SpreadsheetExpressionFunctions.cellDecimalNumberSymbols(),
                    SpreadsheetExpressionFunctions.cellFormattedValue(),
                    SpreadsheetExpressionFunctions.cellFormatter(),
                    SpreadsheetExpressionFunctions.cellFormula(),
                    SpreadsheetExpressionFunctions.cellLocale(),
                    SpreadsheetExpressionFunctions.cellParser(),
                    SpreadsheetExpressionFunctions.cellStyle(),
                    SpreadsheetExpressionFunctions.cellValidator(),
                    SpreadsheetExpressionFunctions.cellValue(),
                    SpreadsheetExpressionFunctions.cellValueType(),
                    SpreadsheetExpressionFunctions.charFunction(),
                    SpreadsheetExpressionFunctions.choose(),
                    SpreadsheetExpressionFunctions.clean(),
                    SpreadsheetExpressionFunctions.code(),
                    SpreadsheetExpressionFunctions.color(),
                    SpreadsheetExpressionFunctions.column(),
                    SpreadsheetExpressionFunctions.columns(),
                    SpreadsheetExpressionFunctions.concat(),
                    SpreadsheetExpressionFunctions.cos(),
                    SpreadsheetExpressionFunctions.count(),
                    SpreadsheetExpressionFunctions.countA(),
                    SpreadsheetExpressionFunctions.countBlank(),
                    SpreadsheetExpressionFunctions.countIf(),
                    SpreadsheetExpressionFunctions.date(),
                    SpreadsheetExpressionFunctions.day(),
                    SpreadsheetExpressionFunctions.days(),
                    SpreadsheetExpressionFunctions.decimal(),
                    SpreadsheetExpressionFunctions.dec2bin(),
                    SpreadsheetExpressionFunctions.dec2hex(),
                    SpreadsheetExpressionFunctions.dec2oct(),
                    SpreadsheetExpressionFunctions.degrees(),
                    SpreadsheetExpressionFunctions.delta(),
                    SpreadsheetExpressionFunctions.dollar(),
                    SpreadsheetExpressionFunctions.e(),
                    SpreadsheetExpressionFunctions.error(),
                    SpreadsheetExpressionFunctions.eval(),
                    SpreadsheetExpressionFunctions.even(),
                    SpreadsheetExpressionFunctions.exact(),
                    SpreadsheetExpressionFunctions.exp(),
                    SpreadsheetExpressionFunctions.falseFunction(),
                    SpreadsheetExpressionFunctions.find(),
                    SpreadsheetExpressionFunctions.fixed(),
                    SpreadsheetExpressionFunctions.floor(),
                    SpreadsheetExpressionFunctions.formatValue(),
                    SpreadsheetExpressionFunctions.formulaText(),
                    SpreadsheetExpressionFunctions.getAlpha(),
                    SpreadsheetExpressionFunctions.getBlue(),
                    SpreadsheetExpressionFunctions.getEnv(),
                    SpreadsheetExpressionFunctions.getGreen(),
                    SpreadsheetExpressionFunctions.getFormatter(),
                    SpreadsheetExpressionFunctions.getFormatValue(),
                    SpreadsheetExpressionFunctions.getLocale(),
                    SpreadsheetExpressionFunctions.getParser(),
                    SpreadsheetExpressionFunctions.getRed(),
                    SpreadsheetExpressionFunctions.getStyle(),
                    SpreadsheetExpressionFunctions.getTextNode(),
                    SpreadsheetExpressionFunctions.getUser(),
                    SpreadsheetExpressionFunctions.getValidator(),
                    SpreadsheetExpressionFunctions.hex2bin(),
                    SpreadsheetExpressionFunctions.hex2dec(),
                    SpreadsheetExpressionFunctions.hex2oct(),
                    SpreadsheetExpressionFunctions.hour(),
                    SpreadsheetExpressionFunctions.hyperlink(),
                    SpreadsheetExpressionFunctions.ifFunction(),
                    SpreadsheetExpressionFunctions.ifs(),
                    SpreadsheetExpressionFunctions.image(),
                    SpreadsheetExpressionFunctions.indirect(),
                    SpreadsheetExpressionFunctions.intFunction(),
                    SpreadsheetExpressionFunctions.invertColor(),
                    SpreadsheetExpressionFunctions.isBlank(),
                    SpreadsheetExpressionFunctions.isDate(),
                    SpreadsheetExpressionFunctions.isErr(),
                    SpreadsheetExpressionFunctions.isError(),
                    SpreadsheetExpressionFunctions.isEven(),
                    SpreadsheetExpressionFunctions.isFormula(),
                    SpreadsheetExpressionFunctions.isLogical(),
                    SpreadsheetExpressionFunctions.isNa(),
                    SpreadsheetExpressionFunctions.isNonText(),
                    SpreadsheetExpressionFunctions.isNull(),
                    SpreadsheetExpressionFunctions.isNumber(),
                    SpreadsheetExpressionFunctions.isOdd(),
                    SpreadsheetExpressionFunctions.isoWeekNum(),
                    SpreadsheetExpressionFunctions.isRef(),
                    SpreadsheetExpressionFunctions.isText(),
                    SpreadsheetExpressionFunctions.lambda(),
                    SpreadsheetExpressionFunctions.left(),
                    SpreadsheetExpressionFunctions.len(),
                    SpreadsheetExpressionFunctions.let(),
                    SpreadsheetExpressionFunctions.list(),
                    SpreadsheetExpressionFunctions.ln(),
                    SpreadsheetExpressionFunctions.log(),
                    SpreadsheetExpressionFunctions.log10(),
                    SpreadsheetExpressionFunctions.lower(),
                    SpreadsheetExpressionFunctions.max(),
                    SpreadsheetExpressionFunctions.maxIf(),
                    SpreadsheetExpressionFunctions.mergeStyle(),
                    SpreadsheetExpressionFunctions.mid(),
                    SpreadsheetExpressionFunctions.min(),
                    SpreadsheetExpressionFunctions.minIf(),
                    SpreadsheetExpressionFunctions.minute(),
                    SpreadsheetExpressionFunctions.mixColor(),
                    SpreadsheetExpressionFunctions.mod(),
                    SpreadsheetExpressionFunctions.month(),
                    SpreadsheetExpressionFunctions.nextEmptyColumn(),
                    SpreadsheetExpressionFunctions.nextEmptyRow(),
                    SpreadsheetExpressionFunctions.not(),
                    SpreadsheetExpressionFunctions.now(),
                    SpreadsheetExpressionFunctions.nullFunction(),
                    SpreadsheetExpressionFunctions.numberValue(),
                    SpreadsheetExpressionFunctions.oct2bin(),
                    SpreadsheetExpressionFunctions.oct2dec(),
                    SpreadsheetExpressionFunctions.oct2hex(),
                    SpreadsheetExpressionFunctions.odd(),
                    SpreadsheetExpressionFunctions.offset(),
                    SpreadsheetExpressionFunctions.or(),
                    SpreadsheetExpressionFunctions.pi(),
                    SpreadsheetExpressionFunctions.print(),
                    SpreadsheetExpressionFunctions.println(),
                    SpreadsheetExpressionFunctions.product(),
                    SpreadsheetExpressionFunctions.proper(),
                    SpreadsheetExpressionFunctions.quotient(),
                    SpreadsheetExpressionFunctions.radians(),
                    SpreadsheetExpressionFunctions.rand(),
                    SpreadsheetExpressionFunctions.randBetween(),
                    SpreadsheetExpressionFunctions.readLine(),
                    SpreadsheetExpressionFunctions.removeEnv(),
                    SpreadsheetExpressionFunctions.replace(),
                    SpreadsheetExpressionFunctions.rept(),
                    SpreadsheetExpressionFunctions.right(),
                    SpreadsheetExpressionFunctions.roman(),
                    SpreadsheetExpressionFunctions.round(),
                    SpreadsheetExpressionFunctions.roundDown(),
                    SpreadsheetExpressionFunctions.roundUp(),
                    SpreadsheetExpressionFunctions.row(),
                    SpreadsheetExpressionFunctions.rows(),
                    SpreadsheetExpressionFunctions.search(),
                    SpreadsheetExpressionFunctions.second(),
                    SpreadsheetExpressionFunctions.setAlpha(),
                    SpreadsheetExpressionFunctions.setBlue(),
                    SpreadsheetExpressionFunctions.setEnv(),
                    SpreadsheetExpressionFunctions.setGreen(),
                    SpreadsheetExpressionFunctions.setLocale(),
                    SpreadsheetExpressionFunctions.setRed(),
                    SpreadsheetExpressionFunctions.setStyle(),
                    SpreadsheetExpressionFunctions.setText(),
                    SpreadsheetExpressionFunctions.sign(),
                    SpreadsheetExpressionFunctions.sin(),
                    SpreadsheetExpressionFunctions.sinh(),
                    SpreadsheetExpressionFunctions.spreadsheetMetadataGet(),
                    SpreadsheetExpressionFunctions.spreadsheetMetadataRemove(),
                    SpreadsheetExpressionFunctions.spreadsheetMetadataSet(),
                    SpreadsheetExpressionFunctions.sqrt(),
                    SpreadsheetExpressionFunctions.style(),
                    SpreadsheetExpressionFunctions.styleGet(),
                    SpreadsheetExpressionFunctions.styleRemove(),
                    SpreadsheetExpressionFunctions.styleSet(),
                    SpreadsheetExpressionFunctions.styledText(),
                    SpreadsheetExpressionFunctions.substitute(),
                    SpreadsheetExpressionFunctions.sum(),
                    SpreadsheetExpressionFunctions.sumIf(),
                    SpreadsheetExpressionFunctions.switchFunction(),
                    SpreadsheetExpressionFunctions.t(),
                    SpreadsheetExpressionFunctions.tan(),
                    SpreadsheetExpressionFunctions.tanh(),
                    SpreadsheetExpressionFunctions.template(),
                    SpreadsheetExpressionFunctions.text(),
                    SpreadsheetExpressionFunctions.textJoin(),
                    SpreadsheetExpressionFunctions.textMatch(),
                    SpreadsheetExpressionFunctions.time(),
                    SpreadsheetExpressionFunctions.today(),
                    SpreadsheetExpressionFunctions.toGray(),
                    SpreadsheetExpressionFunctions.toRgbHexString(),
                    SpreadsheetExpressionFunctions.trim(),
                    SpreadsheetExpressionFunctions.trueFunction(),
                    SpreadsheetExpressionFunctions.trunc(),
                    SpreadsheetExpressionFunctions.type(),
                    SpreadsheetExpressionFunctions.unichar(),
                    SpreadsheetExpressionFunctions.unicode(),
                    SpreadsheetExpressionFunctions.upper(),
                    SpreadsheetExpressionFunctions.validationError(),
                    SpreadsheetExpressionFunctions.value(),
                    SpreadsheetExpressionFunctions.weekDay(),
                    SpreadsheetExpressionFunctions.weekNum(),
                    SpreadsheetExpressionFunctions.year(),
                    SpreadsheetExpressionFunctions.xor()
                )
            )
        );
    }

    /**
     * Stop creation
     */
    private SpreadsheetExpressionFunctionProviders() {
        throw new UnsupportedOperationException();
    }
}
