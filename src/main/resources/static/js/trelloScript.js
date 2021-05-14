/**
 * < ajax 주요 속성 >
 * $(function () {
 *    // html이 로드 됐을 때 실행 할 함수들 정
 * });
 * id 값을 매핑할 땐 -> $('#id값') 이런식으로 한다.
 * class 를 매핑할 땐 -> $('.className')
 * $('#btn').click(function(){
 *     $.ajax({
 *        url: 'url',           // 전송할 url
 *        dataType: 'json',     // 서버가 리턴하는 데이터의 타입
 *        type: 'POST',         // 서버로 전송할 메소드 타입
 *        contentType: 'application/json; charset=utf-8', // 서버로 전송할 데이터의 타입
 *        data: {               // 서버로 보낼 데이터 (현재는 json형식)
 *            title: $('#input-title').val()
 *        },
 *
 *        success: function(result){
 *            성공 시 타게되는 success 함수
 *        },
 *        fail: function(result){
 *            실패 시 타게되는 fail 함수
 *        }
 *     });
 * }
 */

$(function () {
    var $boardList = $('#main-board-lists');
    var $userid = document.getElementById('profile').getAttribute('userid');
    console.log('현재 user의 id는 ' + $userid + ' 입니다.');

    var boardTemplate = "" +
        "            <div class=\"col-md-3\" id='board-list-{{boardId}}' style='margin-right: 20px'>\n" +
        "                <div class=\"card\" data-id={{boardId}}>\n" +
        "                    <span class=\"board-item {{boardColor}}\" data-id={{boardId}} id=\"board-item-{{boardId}}\" type=\"button\" >" + "{{title}}" + "</span>\n" +
        "                    <div class=\"card-body\">\n" +
        "                        <div class=\"d-flex justify-content-end align-items-end\">\n" +
        "                            <div class=\"btn-group\">\n" +
        "                                <button class=\"btn btn-success\" type=\"button\" data-bs-toggle=\"modal\" data-bs-target=\"#exampleModal1\" data-id={{boardId}} id='update'>수정</button>\n" +
        "                                <button type=\"button\" class=\"btn btn-danger\" data-id={{boardId}} id='delete'>삭제</button>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>" +
        "";

    function addBoard(board) {
        $('#div-add-btn-board').before(Mustache.render(boardTemplate, board));
    }

    /**
     * 페이지 로드 시 화면에 보드 리스트를 뿌려줌.
     */
    $.ajax({
        url: 'http://localhost:8080/user/' + $userid + '/boards',
        type: 'GET',

        success: function (result) {
            if (result._embedded != null) {
                var boardList = result._embedded.boardResourceList;

                for (var i = 0; i < boardList.length; i++) {
                    addBoard(boardList[i]);
                }
            }
        }
    });

    /**
     * 보드 추가 이벤트
     */
    $('#save-btn-board-title').click(function () {
        var $close = $('#close-btn-board-modal');
        var $title = $('#input-board-title').val();
        console.log($title);
        if(validateInput($title)) {
            $.ajax({
                url: 'http://localhost:8080/user/' + $userid + '/boards',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    title: $('#input-board-title').val(),
                    boardColor: $('.btn-group input:radio:checked').val()
                }),

                success: function (result) {
                    console.log(result);
                    addBoard(result);
                    $close.click();     // modal 창 close
                    $('#input-board-title').val('');    // input 초기화
                },
                fail: function (result) {
                    alert('통신 실패');
                }
            });
        }
    });

    /**
     * 보드 수정 이벤트
     * 수정할 부분.
     * 문제 : 수정을 누르면-> 처음에는 1번 실행이 잘 되다가, n번째부터 n+1번 중복실행 됨.
     * 해결 : 해당 click 이벤트를 unbind 후 bind 해주면 마지막 이벤트만 수행.
     */
    $boardList.on('click', '#update' , function (e) {
        var $id = $(this).attr('data-id');
        var id = $id;
        console.log($id + '를 수정합니다.');
        var $find = '#board-list-' + $id;
        var $updateOne = $('#main-board-lists').find($find);
        // 중복 실행 방지를 위해 unbind 후 bind 해주었다. (마지막 이벤트만 실행!)
        $('#save-btn-boardDto-title').unbind('click').bind('click', function (e1) {
            if (validateInput($('#input-boardDto-title').val())) {
                $.ajax({
                    url: 'http://localhost:8080/user/' + $userid + '/boards/' + $id,
                    type: 'PATCH',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        title: $('#input-boardDto-title').val(),
                        boardColor: $('#btn-radio-group-update input:radio:checked').val()
                    }),

                    success: function (result) {
                        alert('수정 되었습니다.');
                        $updateOne.html(Mustache.render(boardTemplate, result));
                        $('#close-btn-boardDto-modal').click();     // modal 창 close
                        $('#input-boardDto-title').val('');    // input 초기화
                    }
                });
            }
        });
    });

    /**
     * board 삭제 버튼
     */
    $($boardList).on('click', '#delete', function () {
        var $id = $(this).attr('data-id');
        console.log($id + '를 삭제합니다.');
        var $find = '#board-list-' + $id;
        var $deleteOne = $('#main-board-lists').find($find);

        $.ajax({
            url: 'http://localhost:8080/user/' + $userid + '/boards/' + $id,
            type: 'DELETE',

            success: function (result) {
                alert('삭제되었습니다.');
                $deleteOne.fadeOut(300, function () {
                    $deleteOne.remove();
                });
            }
        });

    });

    /**
     * 보드 타이틀 클릭시 상세 페이지로 이동
     */
    $boardList.on('click', '.card', function (e) {
        if (e.target.id.substring(0,10) == 'board-item') {
            var $id = $(this).attr('data-id');
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/boards/' + $id + '/pockets',

                success: function () {
                    location.href = 'http://localhost:8080/boards/' + $id + '/detail';
                }
            });
        }
    });

    /**
     * 빈 칸 체크
     */
    function validateInput(text){
        if (text == "" || text == null) {
            alert('빈 칸은 입력할 수 없습니다.🙅 다시 입력해주세요.');
            return false;
        }
        return true;
    }

}); /* $(function ()) */