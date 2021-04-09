$(function (){
    var $pocketLists = $('#pocket-lists');
    var $boardId = $pocketLists.attr('boardid');
    var $boardColor = $pocketLists.attr('bgcolor');

    var pocketTemplate = "" +
        "            <div class=\"pocket-item\" id='pocket-item-{{pocketId}}' draggable='true' ondragstart='ondragstart_handler(event)' ondragend='ondragend_handler(event)'>\n" +
        "               <div class=\"card\">\n" +
        "                   <div class=\"card-header\">\n" +
        "                       <span class=\"pocket-title\" id='pocket-title-{{pocketId}}' data-id=\"{{pocketId}}\" type=\"button\">{{title}}</span>\n" +
        "                       <button type=\"button\" class=\"del-btn-pocket\" data-id = \"{{pocketId}}\" aria-label=\"Close\"></button>" +
        "                       <input type=\"hidden\" class=\"form-control\" placeholder=\"insert new title\" id=\"update-pocket-{{pocketId}}\">" +
        "                   </div>\n" +
        "                   <div class=\"card-body\" id=\"pocket-{{pocketId}}-card-list\" ondragover='card_ondragover(event)' ondrop='card_ondrop(event)'>\n" +
        "                   </div>\n" +
        "                   <div class=\"card-footer\">\n" +
        "                       <div class=\"col\">\n" +
        "                       <button class=\"add-btn-card\" type=\"button\" data-id=\"{{pocketId}}\" style=\"width: 100%\" data-bs-toggle='modal' data-bs-target='#add-card-modal'>\n" +
        "                            + Add a card\n" +
        "                       </button>\n" +
        "                       </div>" +
        "                   </div>\n" +
        "               </div>" +
        "            </div>" +
        "";

    var cardTemplate = "" +
        "            <div class=\"col\" id=\"card-item-{{cardId}}\" draggable='true' ondragstart='card_ondragstart(event)'>\n" +
        "                <div class=\"card div-card-item\">\n" +
        "                   <span class=\"card-item\" data-id=\"{{cardId}}\" data-bs-toggle=\"modal\" data-bs-target=\"#show-card-detail-modal\">{{title}}</span>"  +
        "               </div>" +
        "           </div>" +
        "";

    function addPocket(pocket) {
        $('#div-add-btn-pocket').before(Mustache.render(pocketTemplate, pocket));
    }

    function addCard(card, pocketId) {
        var $cardList = '#pocket-' + pocketId + '-card-list';
        $($cardList).append(Mustache.render(cardTemplate, card));
    }

    /**
     *  첫 보드 상세 페이지 조회
     */
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/boards/'+ $boardId + '/pockets',

        success: function (result){
            var bodytag = document.getElementsByTagName('body');
            /* 보드 색상 지정해주기 */
            $('body').addClass('bg-' + $boardColor);
            if(result._embedded != null) {
                var pockets = result._embedded.pocketResourceList;
                for (var i = 0; i < pockets.length; i++) {
                    addPocket(pockets[i]);
                    // card가 있다면 추가
                    if (pockets[i].cards.length > 0) {
                        for (var j = 0; j < pockets[i].cards.length; j++) {
                            addCard(pockets[i].cards[j], pockets[i].pocketId);
                        }
                    }
                }
            }
        }
    })

    /**
     * 포켓 추가 기능
     */
    $('#add-btn-pocket-title').click(function () {
        var $title = $('#input-pocket-title').val();

        if (validateInput($title)) {
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080/boards/' + $boardId + '/pockets',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    title: $title
                }),

                success: function (result) {
                    addPocket(result);
                    $('#close-btn-pocket-title').click();
                    $('#input-pocket-title').val('');
                }
            });
        }
    });

    /**
     *  포켓 타이틀 수정
     */
    $pocketLists.on('click', '.pocket-title', function () {
        var $title = $(this);
        var $id = $(this).attr('data-id');
        console.log('pocket-id : ' + $id + ' (수정 모드)');
        var $input = document.getElementById('update-pocket-' + $id);
        $input.type = 'text';

        // TODO N+1 중복 발생
        var mousedown = false;
        document.addEventListener('mousedown', function (e){
            // alert(e.target + '을 클릭했다.');
            if(mousedown) return;
            else {
                // console.log(e.target)
                if (e.target.id != 'update-pocket-' + $id) {
                    $input.type = 'hidden';
                    mousedown = true;
                }
            }
        });
        var keydown = false;
        $input.addEventListener('keydown', function (e) {
            if(keydown) return;
            else {
                // N+1 문제 해결(2가지) : 1. var keydown 값으로 체크 / 2. event.isComposing은 동작 x
                if ((e.key == 'Enter' && validateInput($('#' + $input.id).val()))) {
                    // TODO ajax 통신 -> 키 엔터 확인까지 마침.
                    console.log($('#' + $input.id).val());
                    $.ajax({
                        type: 'PATCH',
                        url: 'http://localhost:8080/boards/' + $boardId + '/pockets/' + $id,
                        dataType: 'json',
                        contentType: 'application/json',
                        data: JSON.stringify({
                            title: $('#' + $input.id).val(),
                        }),

                        success: function (result) {
                            console.log(result);
                            console.log($title[0].id);
                            $('#' + $title[0].id).text(result.title);
                            $input.type = 'hidden';
                            keydown = true; // keydown 다시 true로 설정.
                        }
                    });
                }
            }
        });
    });

    /**
     *  포켓 삭제 버튼
     */
    $pocketLists.on('click', '.del-btn-pocket', function (e){
        console.log(e.target);
        var $pocketId = $(this).attr('data-id');

        $.ajax({
            type: 'DELETE',
            url: 'http://localhost:8080/boards/' + $boardId + '/pockets/' + $pocketId,

            success: function (result) {
                if(result) {
                    // alert('삭제가 완료되었습니다.');
                    $('#pocket-' + $pocketId).remove();
                }
            },
            fail: function () {
                alert('다시 시도해주세요.');
            },
        });
    });

    /**
     *  카드 아이템 클릭 시 카드 상세 보기
     */
    $pocketLists.on('click', '.card-item', function () {
        var $pocketId = $(this).parent().parent().parent().attr('id').substring(7, 8);
        console.log($pocketId);
        var $id = $(this).attr('data-id');
        var $title = $('#input-card-title-detail');
        var $description = $('#input-card-description-detail');

        $.ajax({
           type: 'GET',
            url: 'http://localhost:8080/pockets/' + $pocketId + '/cards/' + $id,

            success: function (result) {
                $title.val(result.title);
                $description.val(result.description);
                console.log('title: ' + result.title);
                console.log('description: ' + result.description);
            }
        });
        // 카드 상세 보기 중 수정 사항 저장
        $('#save-btn-card-detail-modal').unbind('click').bind('click', function () {
            $.ajax({
               type: 'PATCH',
               url: 'http://localhost:8080/pockets/' + $pocketId + '/cards/' + $id,
               dataType: 'json',
               contentType: 'application/json',
                data: JSON.stringify({
                    title : $('#input-card-title-detail').val(),
                    description : $('#input-card-description-detail').val(),
                }),

                success: function (result) {
                    console.log(result);
                    $('#close-btn-card-detail-modal').click();
                }
            });
        });
    });

    /**
     *  카드 저장 버튼 클릭 시
     */
    $pocketLists.on('click', '.add-btn-card', function (e) {
        var $pocketId = $(this).attr('data-id');
        console.log($pocketId);

        $('#add-btn-card-modal').unbind('click').bind('click' ,function (e) {
            console.log('pocketId : ' + $pocketId);

            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080/pockets/' + $pocketId + '/cards',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    title: $('#input-card-title').val()
                }),

                success: function (result) {
                    console.log(result);
                    addCard(result, $pocketId);
                    $('#close-btn-card-modal').click();
                    $('#input-card-title').val('');
                }
            });
        });
    });

    /**
     *  텍스트 비었는지 체크
     */
    function validateInput(text){
        if (text == "" || text == null) {
            alert('빈 칸은 입력할 수 없습니다.🙅 다시 입력해주세요.');
            return false;
        }
        return true;
    }
});