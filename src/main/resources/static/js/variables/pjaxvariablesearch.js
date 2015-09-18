$(function() {
    $("#query").on('input', function() {
        var form = $('#searchVariables');
        $.pjax({
            container: "#searchResults", 
            data: form.serialize()
        });
    });
});