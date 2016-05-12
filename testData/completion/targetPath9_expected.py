jar_library(
    name='bin',
    dependencies=[
        'before'
        'foo/bar/baz:bar<caret>'
        'after'
    ]
)