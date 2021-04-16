/* start - 드래그 앤 드롭 */
var draggable = null;
var drag_X = 0;

function ondragstart_handler(e){
    drag_X = e.clientX;
    if (e.target.id.substring(0, 4) === 'card') {
        return;
    }
    $('#' + e.target.id).addClass('placeholder');
    console.log('drag start');
    draggable = e.target.id;
    var item = e.dataTransfer.items;

    if (item == undefined || item == null) {
        console.log('drag item is undefined or null!');
        return;
    }
    e.dataTransfer.setData('text/plain', e.target.id); /* dataTransfer는 ondrop에서 사용 가능 */
}

function ondragend_handler(e) {
    $('#' + e.target.id).removeClass('placeholder');
    console.log('drag end');
}

var $dropzone = null;
function ondragover_handler(e) {
    e.preventDefault();
    if (draggable == e.target.id) {
        return;
    }
    if (e.target.className === 'pocket-item') {
        const parent = document.getElementById(e.target.id);
        const child = document.getElementById(draggable);
        $dropzone = e.target.id;
        parent.after(child);
    } else if (e.target.className === 'pocket-title') {
        const id = e.target.id;
        const parentId = id.replace('title', 'item');
        const parent = document.getElementById(parentId);
        const child = document.getElementById(draggable);
        $dropzone = e.target.id;
        parent.before(child);
    }else if (e.target.className === 'btn-add-pocket') {
        const parent = document.getElementById('div-add-btn-pocket');
        const child = document.getElementById(draggable);
        parent.before(child);
    }
}

function getDragAfterElement(y) {
    const draggableElements = [...document.querySelectorAll('.pocket-item:not(.placeholder)')];
    // console.log('length: ' + draggableElements.length);
    draggableElements.reduce((closest, child) => {
        const box = child.getBoundingClientRect();
        const offset = y - box.left - box.width / 2;
        console.log(offset);
    });
}

function ondrop_handler(e) {
    const id = e.dataTransfer.getData('text');
    if (id.substring(0, 4) == 'card') {
        return;
    }
    console.log('옮기려는 아이디: ' + id);
    const draggable1 = document.getElementById(id);
    const dropzone = e.target;
    console.log('드롭할 곳: ' + dropzone.id);
    console.log('진짜 드롭할 곳: ' + $dropzone);
    const $boardId = $('#pocket-lists').attr('boardid');
    const $pocketId = dropzone.id.toString().substring(13, 14);

    /* dropzone이 버튼일 때 제일 마지막에 저장 */
    if (dropzone.id === 'btn-add-pocket' || dropzone.id === 'div-add-btn-pocket') {
        $.ajax({
            type: 'PATCH',
            url: 'http://localhost:8080/boards/' + $boardId + '/pockets/' + id.substring(12,13),
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                boardId: $boardId,
                position: 10000
            }),
            success: function () {
                console.log('drop : success');
            }
        });
    } else {
        console.log('boardID : ' + $boardId);
        console.log(id + '에서 -> ' + $pocketId + '로 위치를 옮깁니다.');
        $.ajax({
            type: 'PATCH',
            url: 'http://localhost:8080/boards/' + $boardId + '/pockets/' + id.substring(12,13),
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                boardId : $boardId,
                position : $pocketId,
            }),

            success: function (result) {
                console.log('drop success');
            }
        });
    }
    e.dataTransfer.clearData();
}

var draggable_card = null;
function card_ondragstart(e) {
    console.log('카드를 드래그했습니다! : ' + e.target);
    var item = e.dataTransfer.items;

    if (item == undefined || item == null) {
        return;
    }
    e.dataTransfer.setData('text/plain', e.target.id);
    draggable_card = e.target.id;
}

function card_dragend(e) {
    $('#' + e.target.id).removeClass('placeholder');
    console.log('drag end');
}

function card_ondragover(e) {
    if (e.target.id == '') {
        return ;
    }
    e.preventDefault();
    const parent = document.getElementById(e.target.id);
    const child = document.getElementById(draggable_card);
    console.log('parent : ' + parent);
    console.log('child : ' + child);

    parent.appendChild(child);
}

function card_ondrop(e) {
    const id = e.dataTransfer.getData('text');
    console.log('ondrop!: ' + id);
    if (id.substring(0, 9) == 'card-item') {
        console.log('great!');
    }
}
/* end - 드래그 앤 드롭*/

$(function (){
    var $pocketList = $('#pocket-list');
    var $boardId = $pocketList.attr('boardid');
    var $boardColor = $pocketList.attr('bgcolor');

    var pocketTemplate = "" +
        "            <div class=\"pocket-item\" id='pocket-item-{{pocketId}}' draggable='true' ondragstart='ondragstart_handler(event)' ondragend='ondragend_handler(event)' ondragover='ondragover_handler(event)' ondrop='ondrop_handler(event)'>\n" +
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
        "            <div class=\"col\" id=\"card-item-{{cardId}}\" draggable='true' ondragstart='card_ondragstart(event)' ondragend='card_ondragend(event)'>\n" +
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
    $pocketList.on('click', '.pocket-title', function () {
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
    $pocketList.on('click', '.del-btn-pocket', function (e){
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
    $pocketList.on('click', '.card-item', function () {
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
    $pocketList.on('click', '.add-btn-card', function (e) {
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