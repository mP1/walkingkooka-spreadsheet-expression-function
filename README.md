[![Build Status](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-spreadsheet-expression-function/badge.svg)](https://coveralls.io/github/mP1/walkingkooka-spreadsheet-expression-function)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka-spreadsheet-expression-function)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

### [Functions](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/blob/master/src/main/java/walkingkooka/spreadsheet/expression/function/SpreadsheetExpressionFunctions.java)

An assembly of functions that faithfully match their Excel equivalents in terms of functionality and errors.

This includes an assembly of other general purpose `ExpressionFunction(s)` from other repos.

- [boolean](https://github.com/mP1/walkingkooka-tree-expression-function-boolean)
- [color](https://github.com/mP1/walkingkooka-color/tree/master/src/main/java/walkingkooka/color/expression/function)
- [datetime](https://github.com/mP1/walkingkooka-tree-expression-function-datetime)
- [engineering](https://github.com/mP1/walkingkooka-tree-expression-function-engineering)
- [environment](https://github.com/mP1/walkingkooka-environment-expression-function)
- [json](https://github.com/mP1/walkingkooka-tree-json)
- [net](https://github.com/mP1/walkingkooka-tree-expression-function-net)
- [net-expression-function](https://github.com/mP1/walkingkooka-net-expression-function/tree/master/src/main/java/walkingkooka/net/expression/function)
- [number](https://github.com/mP1/walkingkooka-tree-expression-function-number)
- [number-trigonometry](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry)
- [stat](https://github.com/mP1/walkingkooka-tree-expression-function-stat)
- [storage-expression-function](https://github.com/mP1/walkingkooka-storage-expression-function)
- [string](https://github.com/mP1/walkingkooka-tree-expression-function-string)
- [terminal](https://github.com/mP1/walkingkooka-terminal)
- [tree-text](https://github.com/mP1/walkingkooka-tree-text)
- [validation](https://github.com/mP1/walkingkooka-validation/)

The two links below provide links to available functions in Excel and Sheets

- [Excel](https://support.microsoft.com/en-au/office/excel-functions-alphabetical-b3944572-255d-4efb-bb96-c6d90033e188)
- [Sheets](https://support.google.com/docs/table/25273?hl=en)

## [Functions](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)

Google Sheets and Microsoft Excel have over 500 functions.

- [Official Google sheets functions (513)](https://support.google.com/docs/table/25273?hl=en)
- [Official Microsoft Excel functions](https://support.microsoft.com/en-us/office/excel-functions-alphabetical-b3944572-255d-4efb-bb96-c6d90033e188)

The list below (with 263) is incomplete and constantly being updated as new functions are added. A small number are not
found in Sheets or Excel and may be used to interact with features not found in those two platforms.

- [abs](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- [acos](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- address
- [and](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionLogicalAnd.java)
- [asin](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- [atan](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- average
- averageIf
- [badge](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionBadge.java)
- [base](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/StringExpressionFunctionBase.java)
- [bin2dec](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [bin2hex](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [bin2oct](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [bitand](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/NumberExpressionFunctionBitAndOrXor.java)
- [bitor](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/NumberExpressionFunctionBitAndOrXor.java)
- [bitxor](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/NumberExpressionFunctionBitAndOrXor.java)
- [border](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionBorder.java)
- [ceil](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- cell
- cellCurrency
- cellDateTimeSymbols
- cellDecimalNumberSymbols
- cellFormattedValue
- cellFormatter
- cellFormula
- cellLocale
- cellParser
- cellStyle
- cellValidator
- cellValue
- cellValueType
- char
- [choose](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/ObjectExpressionFunctionChoose.java)
- clean
- code
- [color](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionColor.java)
- column
- columns
- concat
- [cos](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- [cosh](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- count
- countA
- countBlank
- countIf
- createSpreadsheetMetadata
- [currency](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionCurrency.java)
- [currencyCode](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionCurrencyCode.java)
- [currencyValue](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionCurrencyValue.java)
- [date](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/LocalDateExpressionFunctionDate.java)
- [day](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionLocalDateComponent.java)
- [days](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionDays.java)
- [dec2bin](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [dec2hex](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [dec2oct](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [decimal](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionDecimal.java)
- [degrees](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- deleteSpreadsheetMetadata
- [deleteStorage](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionDelete.java)
- [delta](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/BooleanExpressionFunctionDelta.java)
- dollar
- [e](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionConstants.java)
- [emailAddress](https://github.com/mP1/walkingkooka-net-expression-function/blob/master/src/main/java/walkingkooka/net/expression/function/NetExpressionFunctionEmailAddress.java)
- error
- [eval](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionEval.java)
- [even](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- exact
- [exit](https://github.com/mP1/walkingkooka-terminal/tree/master/src/main/java/walkingkooka/terminal/expression/function/TerminalExpressionFunctionExit.java)
- exp
- [false](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionFalse.java)
- find
- [fixed](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/StringExpressionFunctionFixed.java)
- [flag](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionFlag.java)
- [floor](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- [formatValue](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/blob/master/src/main/java/walkingkooka/spreadsheet/expression/function/SpreadsheetExpressionFunctionObjectFormatValue.java)
- formulatext
- [getAlpha](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionGetAlpha.java)
- [getBlue](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionGetBlue.java)
- [getCharset](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetCharset.java)
- [getCurrency](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetCurrency.java)
- [getCurrentWorkingDirectory](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionGetCurrentWorkingDirectory.java)
- getDateTimeSymbols
- getDecimalNumberSymbols
- [getEnv](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetEnv.java)
- getFormatter
- getFormatValue
- getFormulaText
- [getGreen](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionGetGreen.java)
- [getHomeDirectory](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionGetHomeDirectory.java)
- [getHost](https://github.com/mP1/walkingkooka-net-expression-function/blob/master/src/main/java/walkingkooka/net/expression/function/NetExpressionFunctionGetHost.java)
- [getIndentation](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetIndentation.java)
- [getLineEnding](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetLineEnding.java)
- [getLocale](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetLocale.java)
- getParser
- [getRed](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionGetRed.java)
- getSpreadsheetMetadataValue
- [getStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionGetStyle.java)
- [getTextNode](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionGetTextNode.java)
- [getTimeOffset](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetTimeOffset.java)
- [getUser](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetUser.java)
- [getValidator](https://github.com/mP1/walkingkooka-validation/blob/master/src/main/java/walkingkooka/validation/expression/function/ValidationExpressionFunctionGetValidator.java)
- getValue
- [hex2bin](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [hex2dec](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [hex2oct](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [hour](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionLocalTime.java)
- [hyperlink](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionHyperlink.java)
- [if](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/ObjectExpressionFunctionIf.java)
- [ifs](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/ObjectExpressionFunctionIfs.java)
- [image](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionImage.java)
- indirect
- [int](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- [invertColor](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionInvertColor.java)
- isBlank
- [isDate](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/BooleanExpressionFunctionIsDate.java)
- isErr
- isError
- [isEven](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/BooleanExpressionFunctionIsEvenIsOdd.java)
- isFormula
- isLogical
- isNa
- isNonText
- [isNull](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionIsNull.java)
- [isNumber](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/BooleanExpressionFunctionIsNumber.java)
- [isOdd](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/BooleanExpressionFunctionIsEvenIsOdd.java)
- [isoWeeknum](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionIsoWeekNum.java)
- [isRef](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionIsReference.java)
- isText
- [json](https://github.com/mP1/walkingkooka-tree-json/blob/master/src/main/java/walkingkooka/tree/json/expression/function/JsonNodeExpressionFunctionJson.java)
- [lambda](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionLambda.java)
- left
- len
- let
- [list](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionListSharedList.java)
- [listNonNull](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionListSharedListNonNull.java)
- [listStorage](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionList.java)
- [ln](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- loadSpreadsheetMetadata
- [locale](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionLocale.java)
- [localeLanguageTag](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionLocaleLanguageTag.java)
- [log](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionLog.java)
- [log10](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- lower
- [margin](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionMargin.java)
- max
- maxIf
- [mergeStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionMergeStyle.java)
- mid
- min
- minIf
- [minute](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionLocalTime.java)
- [mixColor](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionMixColor.java)
- [mod](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionMod.java)
- [month](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionLocalDateComponent.java)
- nextEmptyColumn
- nextEmptyRow
- [not](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionNot.java)
- [now](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/LocalDateTimeExpressionFunctionNow.java)
- [null](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionNull.java)
- numberValue
- [oct2bin](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [oct2dec](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [oct2hex](https://github.com/mP1/walkingkooka-tree-expression-function-engineering/blob/master/src/main/java/walkingkooka/tree/expression/function/engineering/StringExpressionFunctionParseBase2Base.java)
- [odd](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- offset
- [or](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionLogicalOr.java)
- [padding](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionPadding.java)
- [pi](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionConstants.java)
- [print](https://github.com/mP1/walkingkooka-terminal/tree/master/src/main/java/walkingkooka/terminal/expression/function/TerminalExpressionFunctionPrint.java)
- [printEnv](https://github.com/mP1/walkingkooka-terminal/tree/master/src/main/java/walkingkooka/terminal/expression/function/TerminalExpressionFunctionPrintEnv.java)
- [println](https://github.com/mP1/walkingkooka-terminal/tree/master/src/main/java/walkingkooka/terminal/expression/function/TerminalExpressionFunctionPrintln.java)
- [product](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionProduct.java)
- proper
- [quotient](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionQuotient.java)
- [radians](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- [random](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionRandom.java)
- [randomBetween](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionRandomBetween.java)
- [readLine](https://github.com/mP1/walkingkooka-terminal/tree/master/src/main/java/walkingkooka/terminal/expression/function/TerminalExpressionFunctionReadLine.java)
- [readStorage](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionRead.java)
- [readStorageText](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionReadText.java)
- [removeEnv](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionRemoveEnv.java)
- removeSpreadsheetMetadataValue
- replace
- rept
- right
- [roman](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/StringExpressionFunctionRoman.java)
- [round](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- [roundDown](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionRoundDownHalfUp.java)
- [roundUp](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionRoundDownHalfUp.java)
- row
- rows
- saveSpreadsheetMetadata
- [script](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionScript.java)
- search
- [second](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionLocalTime.java)
- [setAlpha](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionSetAlpha.java)
- [setBlue](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionSetBlue.java)
- [setCharset](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetCharset.java)
- [setCurrency](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetCurrency.java)
- [setCurrentWorkingDirectory](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionSetCurrentWorkingDirectory.java)
- [setEnv](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetEnv.java)
- [setGreen](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionSetGreen.java)
- [setHomeDirectory](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionSetHomeDirectory.java)
- [setHost](https://github.com/mP1/walkingkooka-net-expression-function/blob/master/src/main/java/walkingkooka/net/expression/function/NetExpressionFunctionSetHost.java)
- [setIndentation](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetIndentation.java)
- [setLineEnding](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetLineEnding.java)
- [setLocale](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetLocale.java)
- [setRed](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionSetRed.java)
- setSpreadsheetMetadataValue
- [setStyle](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionSetStyle.java)
- [setText](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionSetText.java)
- [setTimeOffset](https://github.com/mP1/walkingkooka-environment-expression-function/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetTimeOffset.java)
- [shell](https://github.com/mP1/walkingkooka-terminal/tree/master/src/main/java/walkingkooka/terminal/expression/function/TerminalExpressionFunctionShell.java)
- [sign](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- [sin](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- [sinh](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- spreadsheetUrl
- [sqrt](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionUnary.java)
- [style](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyle.java)
- [styledText](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionStyledText.java)
- [styleGet](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleGet.java)
- [styleRemove](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleRemove.java)
- [styleRemoveIf](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleRemoveIf.java)
- [styleReplaceIf](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleReplaceIf.java)
- [styleSet](https://github.com/mP1/walkingkooka-tree-text/blob/master/src/main/java/walkingkooka/tree/text/expression/function/TreeTextExpressionFunctionTextStyleSet.java)
- substitute
- sum
- sumIf
- [switch](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/ObjectExpressionFunctionSwitch.java)
- t
- [tan](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- [tanh](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry/blob/master/src/main/java/walkingkooka/tree/expression/function/number/trigonometry/NumberExpressionFunction.java)
- template
- text
- textJoin
- textMatch
- [time](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/LocalTimeExpressionFunctionTime.java)
- [today](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/LocalDateExpressionFunctionToday.java)
- [toGray](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionRgbColorToGray.java)
- [toHslColor](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionToHslColor.java)
- [toHsvColor](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionToHsvColor.java)
- [toRgbColor](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionToRgbColor.java)
- [toRgbHexString](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionToRgbHexString.java)
- [toWebColorName](https://github.com/mP1/walkingkooka-color/blob/master/src/main/java/walkingkooka/color/expression/function/ColorExpressionFunctionToWebColorName.java)
- trim
- [true](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionTrue.java)
- [trunc](https://github.com/mP1/walkingkooka-tree-expression-function-number/blob/master/src/main/java/walkingkooka/tree/expression/function/number/NumberExpressionFunctionTrunc.java)
- type
- unichar
- unicode
- upper
- [url](https://github.com/mP1/walkingkooka-net-expression-function/blob/master/src/main/java/walkingkooka/net/expression/function/NetExpressionFunctionUrl.java)
- [validationChoiceList](https://github.com/mP1/walkingkooka-validation/blob/master/src/main/java/walkingkooka/validation/expression/function/ValidationExpressionFunctionValidationChoiceList.java)
- validationError
- [validationErrorIf](https://github.com/mP1/walkingkooka-validation/blob/master/src/main/java/walkingkooka/validation/expression/function/ValidationExpressionFunctionValidationErrorIf.java)
- [validationValue](https://github.com/mP1/walkingkooka-validation/blob/master/src/main/java/walkingkooka/validation/expression/function/ValidationExpressionFunctionValidationValue.java)
- value
- [weekday](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionIsoWeekNum.java))
- [weeknum](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionLocalDateWeekdayWeekNum.java)
- [writeStorage](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionWrite.java)
- [writeStorageText](https://github.com/mP1/walkingkooka-storage-expression-function/blob/master/src/main/java/walkingkooka/storage/expression/function/StorageExpressionFunctionWriteText.java)
- [xor](https://github.com/mP1/walkingkooka-tree-expression-function-boolean/blob/master/src/main/java/walkingkooka/tree/expression/function/booleann/BooleanExpressionFunctionLogicalXor.java)
- [year](https://github.com/mP1/walkingkooka-tree-expression-function-datetime/blob/master/src/main/java/walkingkooka/tree/expression/function/datetime/NumberExpressionFunctionLocalDateComponent.java)

Many more functions are outstanding and remain [TODO](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues).

# SpreadsheetForm functions

These functions exist to support forms that display and store cell values in the enclosing spreadsheet.

- nextEmptyColumn
- validationError

# Query functions

The following functions are only useful within a search context, each extracting
a [SpreadsheetCell](https://github.com/mP1/walkingkooka-spreadsheet/blob/master/src/main/java/walkingkooka/spreadsheet/SpreadsheetCell.java)
so the value may be used by a predicate to filter cells. They are not intended to be used with formula expressions.

- cellCurrency
- cellDateTimeSymbols
- cellDecimalNumberSymbols
- cellFormattedValue
- cellFormatter
- cellFormula
- cellLocale
- cellParser
- cellStyle
- cellValidator
- cellValue
- cellValueType
- textMatch

# SpreadsheetMetadata functions

These functions are not intended to be used within a spreadsheet formula, but rather provide utility for other
purposes (TODO share example)

- spreadsheetMetadataGet
- spreadsheetMetadataRemove
- spreadsheetMetadataSet

