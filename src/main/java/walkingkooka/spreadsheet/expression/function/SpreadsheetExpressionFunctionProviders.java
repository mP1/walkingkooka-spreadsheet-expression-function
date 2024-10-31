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

import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProviders;

/**
 * Provider getter.
 */
public final class SpreadsheetExpressionFunctionProviders implements PublicStaticHelper {

    /**
     * An {@link ExpressionFunctionProvider} with all the functions in this project.
     */
    public static ExpressionFunctionProvider expressionFunctionProvider(final CaseSensitivity nameCaseSensitivity) {
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
                                SpreadsheetExpressionFunctions.base(),
                                SpreadsheetExpressionFunctions.bin2dec(),
                                SpreadsheetExpressionFunctions.bin2hex(),
                                SpreadsheetExpressionFunctions.bin2oct(),
                                SpreadsheetExpressionFunctions.bitAnd(),
                                SpreadsheetExpressionFunctions.bitOr(),
                                SpreadsheetExpressionFunctions.bitXor(),
                                SpreadsheetExpressionFunctions.ceil(),
                                SpreadsheetExpressionFunctions.cell(),
                                SpreadsheetExpressionFunctions.cellFormattedValue(),
                                SpreadsheetExpressionFunctions.cellFormatter(),
                                SpreadsheetExpressionFunctions.cellFormula(),
                                SpreadsheetExpressionFunctions.cellParser(),
                                SpreadsheetExpressionFunctions.cellStyle(),
                                SpreadsheetExpressionFunctions.cellValue(),
                                SpreadsheetExpressionFunctions.charFunction(),
                                SpreadsheetExpressionFunctions.choose(),
                                SpreadsheetExpressionFunctions.clean(),
                                SpreadsheetExpressionFunctions.code(),
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
                                SpreadsheetExpressionFunctions.even(),
                                SpreadsheetExpressionFunctions.exact(),
                                SpreadsheetExpressionFunctions.exp(),
                                SpreadsheetExpressionFunctions.falseFunction(),
                                SpreadsheetExpressionFunctions.find(),
                                SpreadsheetExpressionFunctions.fixed(),
                                SpreadsheetExpressionFunctions.floor(),
                                SpreadsheetExpressionFunctions.formulaText(),
                                SpreadsheetExpressionFunctions.hex2bin(),
                                SpreadsheetExpressionFunctions.hex2dec(),
                                SpreadsheetExpressionFunctions.hex2oct(),
                                SpreadsheetExpressionFunctions.hour(),
                                SpreadsheetExpressionFunctions.hyperlink(),
                                SpreadsheetExpressionFunctions.ifFunction(),
                                SpreadsheetExpressionFunctions.ifs(),
                                SpreadsheetExpressionFunctions.indirect(),
                                SpreadsheetExpressionFunctions.intFunction(),
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
                                SpreadsheetExpressionFunctions.ln(),
                                SpreadsheetExpressionFunctions.log(),
                                SpreadsheetExpressionFunctions.log10(),
                                SpreadsheetExpressionFunctions.lower(),
                                SpreadsheetExpressionFunctions.max(),
                                SpreadsheetExpressionFunctions.maxIf(),
                                SpreadsheetExpressionFunctions.mid(),
                                SpreadsheetExpressionFunctions.min(),
                                SpreadsheetExpressionFunctions.minIf(),
                                SpreadsheetExpressionFunctions.minute(),
                                SpreadsheetExpressionFunctions.mod(),
                                SpreadsheetExpressionFunctions.month(),
                                SpreadsheetExpressionFunctions.not(),
                                SpreadsheetExpressionFunctions.now(),
                                SpreadsheetExpressionFunctions.nullValue(),
                                SpreadsheetExpressionFunctions.numberValue(),
                                SpreadsheetExpressionFunctions.oct2bin(),
                                SpreadsheetExpressionFunctions.oct2dec(),
                                SpreadsheetExpressionFunctions.oct2hex(),
                                SpreadsheetExpressionFunctions.odd(),
                                SpreadsheetExpressionFunctions.offset(),
                                SpreadsheetExpressionFunctions.or(),
                                SpreadsheetExpressionFunctions.pi(),
                                SpreadsheetExpressionFunctions.product(),
                                SpreadsheetExpressionFunctions.proper(),
                                SpreadsheetExpressionFunctions.quotient(),
                                SpreadsheetExpressionFunctions.radians(),
                                SpreadsheetExpressionFunctions.rand(),
                                SpreadsheetExpressionFunctions.randBetween(),
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
                                SpreadsheetExpressionFunctions.sign(),
                                SpreadsheetExpressionFunctions.sin(),
                                SpreadsheetExpressionFunctions.sinh(),
                                SpreadsheetExpressionFunctions.sqrt(),
                                SpreadsheetExpressionFunctions.substitute(),
                                SpreadsheetExpressionFunctions.sum(),
                                SpreadsheetExpressionFunctions.sumIf(),
                                SpreadsheetExpressionFunctions.switchFunction(),
                                SpreadsheetExpressionFunctions.t(),
                                SpreadsheetExpressionFunctions.tan(),
                                SpreadsheetExpressionFunctions.tanh(),
                                SpreadsheetExpressionFunctions.text(),
                                SpreadsheetExpressionFunctions.textJoin(),
                                SpreadsheetExpressionFunctions.textMatch(),
                                SpreadsheetExpressionFunctions.time(),
                                SpreadsheetExpressionFunctions.today(),
                                SpreadsheetExpressionFunctions.trim(),
                                SpreadsheetExpressionFunctions.trueFunction(),
                                SpreadsheetExpressionFunctions.trunc(),
                                SpreadsheetExpressionFunctions.type(),
                                SpreadsheetExpressionFunctions.unichar(),
                                SpreadsheetExpressionFunctions.unicode(),
                                SpreadsheetExpressionFunctions.upper(),
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
