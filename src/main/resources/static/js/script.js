$(document).ready(function () {
    function getCurrentUser() {
        (async () => {
            let url = "/api/users/" + userId;
            let response = await fetch(url);
            let userObj = await response.json();

            $("#userEmail").text(userObj.email);
            $("#userAuthority").text("with roles: " + userObj.role);
            let table = $("#userInformationTab table tbody");
            table.empty();
            let tableRow = $("<tr>");
            $.each(userObj, function (key, val) {
                if ("id" === key) {
                    tableRow.append("<th>" + val + "</th>");
                    return;
                }
                tableRow.append("<td>" + val + "</td>");
            });
            table.append(tableRow);
        })()
    }

    getCurrentUser();
    $("#userInformationTabButton").click(getCurrentUser);

    function buttonEventListener(elem, formAction, formId) {
        let tableRow = $(elem).closest("tr");
        let id = tableRow.find("th").html();
        let name = tableRow.find("td").eq(0).html();
        let lastName = tableRow.find("td").eq(1).html();
        let age = tableRow.find("td").eq(2).html();
        let email = tableRow.find("td").eq(3).html();
        let role = tableRow.find("td").eq(4).html();
        $(".modal-content").load($(elem).attr("href"), function () {
            $(".modal-content form").prop("action", formAction);
            $(".modal-content form").prop("id", formId);
            $(".modal-content input[name=id]").val(id);
            $(".modal-content input[name=name]").val(name);
            $(".modal-content input[name=lastName]").val(lastName);
            $(".modal-content input[name=age]").val(age);
            $(".modal-content input[name=email]").val(email);
            $(".modal-content select[name=role]").val(role);
        });
        $("#editDeleteUserModal").modal("show");
    }

    $("#usersTableTabButton").click(function (e) {
        e.preventDefault();
        (async () => {
            let url = "/api/users/";
            let response = await fetch(url);
            let userList = await response.json();

            let table = $("#usersTableTab table tbody");
            table.empty();
            $.each(userList, function (i, userObj) {
                let tableRow = $("<tr>");
                let id = -1;
                $.each(userObj, function (key, val) {
                    if ("id" === key) {
                        id = val;
                        tableRow.append("<th>" + id + "</th>");
                        return;
                    }
                    tableRow.append("<td>" + val + "</td>");
                });
                let editButton = $("<a/>", {
                    text: "Edit",
                    id: "editUserButton",
                    class: "btn btn-info",
                    href: "/users/edit",
                    click: function (e) {
                        e.preventDefault();
                        buttonEventListener($(this), "/api/users", "editUserForm");
                    }
                });
                let deleteButton = $("<a/>", {
                    text: "Delete",
                    id: "deleteUserButton",
                    class: "btn btn-danger",
                    href: "/users/delete",
                    click: function (e) {
                        e.preventDefault();
                        buttonEventListener($(this), "/api/users/" + id, "deleteUserForm");
                    }
                });
                tableRow.append($(editButton).wrap("<td></td>").parent());
                tableRow.append($(deleteButton).wrap("<td></td>").parent());
                table.append(tableRow);
            });
        })()
    }).trigger("click");
});

$(document).on("submit", "#deleteUserForm", function (e) {
    e.preventDefault();
    (async () => {
        let url = $(this).attr("action");
        let response = await fetch(url, {
            method: "DELETE"
        });
        if (response.ok) {
            $("#editDeleteUserModal").modal("hide");
            $("#usersTableTabButton").click();
        }
    })()
});

$(document).on("submit", "#editUserForm", function (e) {
    e.preventDefault();
    (async () => {
        let url = $(this).attr("action");
        let response = await fetch(url, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json;charset=utf-8"
            },
            body: JSON.stringify(Object.fromEntries(new FormData(this)))
        });
        if (response.ok) {
            $("#editDeleteUserModal").modal("hide");
            $("#usersTableTabButton").click();
        }
    })()
});

$(document).on("submit", "#newUserForm", function (e) {
    e.preventDefault();
    (async () => {
        let url = $(this).attr("action");
        let response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json;charset=utf-8"
            },
            body: JSON.stringify(Object.fromEntries(new FormData(this)))
        });
        if (response.ok) {
            $(this)[0].reset();
            $("#usersTableTabButton").click();
        }
    })()
});
