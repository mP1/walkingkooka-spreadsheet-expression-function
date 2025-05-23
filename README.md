[![Build Status](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-spreadsheet-expression-function/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-spreadsheet-expression-function?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka-spreadsheet-expression-function)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

# walkingkooka-spreadsheet-expression-function

An assembly of functions that faithfully match their Excel equivalents in terms of functionality and errors.

This includes an assembly of other general purpose `ExpressionFunction(s)` from other repos.

- [boolean](https://github.com/mP1/walkingkooka-tree-expression-function-boolean)
- [datetime](https://github.com/mP1/walkingkooka-tree-expression-function-datetime)
- [engineering](https://github.com/mP1/walkingkooka-tree-expression-function-engineering)
- [net](https://github.com/mP1/walkingkooka-tree-expression-function-net)
- [number](https://github.com/mP1/walkingkooka-tree-expression-function-number)
- [number-trigonometry](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry)
- [stat](https://github.com/mP1/walkingkooka-tree-expression-function-stat)
- [string](https://github.com/mP1/walkingkooka-tree-expression-function-string)

The two links below provide links to available functions in Excel and Sheets

- [Excel](https://support.microsoft.com/en-au/office/excel-functions-alphabetical-b3944572-255d-4efb-bb96-c6d90033e188)
- [Sheets](https://support.google.com/docs/table/25273?hl=en)

Note the list is incomplete when compared to Excel and Sheets and should be considered a work in progress. The vast
majority of these functions are formula expression functions.

- abs
- acos
- address
- and
- asin
- atan
- average
- averageIf
- base
- bin2dec
- bin2hex
- bin2oct
- bitand
- bitor
- bitxor
- ceil
- cell
- cellDateTimeSymbols
- cellDecimalNumberSymbols
- cellFormattedValue
- cellFormatter
- cellFormula
- cellParser
- cellStyle
- cellValue
- char
- choose
- clean
- code
- column
- columns
- concat
- cos
- count
- countA
- countBlank
- countIf
- date
- day
- days
- dec2bin
- dec2hex
- dec2oct
- decimal
- degrees
- delta
- dollar
- e
- error
- eval
- even
- exact
- exp
- false
- find
- fixed
- floor
- formulatext
- hex2bin
- hex2dec
- hex2oct
- hour
- hyperlink
- if
- ifs
- indirect
- int
- isBlank
- isDate
- isErr
- isError
- isEven
- isFormula
- isLogical
- isNa
- isNonText
- isNull
- isNumber
- isOdd
- isRef
- isText
- isoweeknum
- lambda
- left
- len
- let
- ln
- log
- log10
- lower
- max
- maxIf
- mid
- min
- minIf
- minute
- mod
- month
- not
- now
- null
- numberValue
- oct2bin
- oct2dec
- oct2hex
- odd
- offset
- or
- pi
- product
- proper
- quotient
- radians
- rand
- randBetween
- replace
- rept
- right
- roman
- round
- roundDown
- roundUp
- row
- rows
- search
- second
- sign
- sin
- sinh
- sqrt
- substitute
- sum
- sumIf
- switch
- t
- tan
- tanh
- text
- textJoin
- textMatch
- time
- today
- trim
- true
- trunc
- type
- unichar
- unicode
- upper
- value
- weekday
- weeknum
- xor
- year

Many more functions are outstanding and remain [TODO](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues).

# Search functions

The following functions are only useful within a search context, each extracting a
[SpreadsheetCell](https://github.com/mP1/walkingkooka-spreadsheet/blob/master/src/main/java/walkingkooka/spreadsheet/SpreadsheetCell.java)
so the value may be used by a predicate to filter cells. They are not intended to be used with formula expressions.

- cellFormattedValue
- cellFormatter
- cellFormula
- cellParser
- cellStyle
- cellValue
- cellValidator
- textMatch

# SpreadsheetMetadata functions

These functions are not intended to be used within a spreadsheet formula, but rather provide utility for other
purposes (TODO share example)

- spreadsheetMetadataGet
- spreadsheetMetadataRemove
- spreadsheetMetadataSet

# SpreadsheetForm function

These functions exist to support forms that display and store cell values in the enclosing spreadsheet.

- nextEmptyColumn
- validationError