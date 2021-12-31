$(document).ready(function () {
    // with plugin options
    $("#input-id").fileinput({
        uploadUrl: "/sourceSubmit",
        // uploadAsync: false,
        language: 'zh',
    })
    $("#input-id").on("fileuploaded", function (event, data, previewId, index) {
     console.log('event = ', event)
        console.log('data = ', data)
        let rows = data.response.results;
        $(".remove").remove();
        for(var i = 0; i < rows.length; i++) {
            var name = rows[i].name;
            var max_score = rows[i].max_score;
            var score = rows[i].score;
            var html = $(".tr1clone").clone();
            html.removeClass("tr1clone").show().addClass("remove");
            html.find(".td1").text(name)
            html.find(".score").text(score)
            html.find(".max_score").text(max_score)
            if(score == 0){
                html.find(".progress-bar0").show();
            }
            if(score == 1){
                html.find(".progress-bar1").show();
            }
            if(score == 2){
                html.find(".progress-bar-warning").show();
            }
            if(score == 3){
                html.find(".progress-bar-success").show();
            }

            $("#tb1").append(html);
        }


    })
});
