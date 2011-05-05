$(function() {
    $.history.init(function (url) {
        if (url != "") {
            $("#page").load(url, function () {
                console.info(url);
                $("#page").hide().fadeIn();
                $("#page a[href*=ajax]").click(function() {
                    $.history.load("ajax" + this.href.split("ajax")[1].replace(/ |%20/g, "+"));
                    return false;
                });
            });
        }
    });
    $("#search").submit(function() {
        $.history.load("ajax/search.jsp?query=" + $("input[name=query]").val().replace(/ /g, "+"));
        return false;
    }).find("> input[name=query]").focus(function() {
        this.oldValueue = $(this).val();
        $(this).val("");
    }).blur(function() {
        if ($(this).val() == "") {
            $(this).val(this.oldValueue);
        }
    });

    $("a[href*=ajax]").click(function() {
        $.history.load("ajax" + this.href.split("ajax")[1].replace(/ |%20/g, "+"));
        return false;
    });
});