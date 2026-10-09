/* IASF Salary Management System - jQuery interactions */
$(document).ready(function () {

    // Reset Form button action
    $('#btnResetForm').on('click', function (e) {
        e.preventDefault();
        // If in edit mode, redirect back to /users to reset form state
        if ($('#userId').val()) {
            window.location.href = '/users';
            return;
        }

        // Clear input values
        $('#userName').val('');
        $('#userAge').val('');
        $('#userSalary').val('');
        $('.text-error').remove();
        $('.form-control-custom').removeClass('is-invalid');
    });

    // Confirmation for Remove button
    $('.btn-remove-user').on('click', function (e) {
        const userName = $(this).data('username') || 'this user';
        if (!confirm('Are you sure you want to remove ' + userName + '?')) {
            e.preventDefault();
        }
    });

    // Auto fadeout success alerts after 5 seconds
    setTimeout(function () {
        $('.alert-custom-success').fadeOut('slow');
    }, 5000);
});
