package com.Edulink.EdulinkServer.service;


import com.Edulink.EdulinkServer.model.StudentInfo;

public class EmailTemplates {


    public static String resetPasswordHtml(String resetLink) {
        return """
        <!doctype html>
        <html lang="en">
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1">
          <title>Password Reset</title>
          <style>
            /* Inline-friendly minimal styles */
            .container{max-width:600px;margin:0 auto;padding:24px;background:#ffffff;border:1px solid #eaeaea;border-radius:12px;font-family:Arial,Helvetica,sans-serif;color:#111}
            .brand{display:flex;align-items:center;gap:10px}
            .logo{width:36px;height:36px;border-radius:8px;background:#111;display:inline-block}
            .title{font-size:20px;margin:0}
            .muted{color:#666}
            .btn{display:inline-block;padding:12px 18px;border-radius:8px;text-decoration:none}
            .primary{background:#111;color:#fff}
            .link{word-break:break-all;color:#0a66c2;text-decoration:none}
            .footer{font-size:12px;color:#888;margin-top:24px}
          </style>
        </head>
        <body style="background:#f5f6f8;padding:24px;">
          <div class="container">
            <div class="brand">
              <span class="logo"></span>
              <h1 class="title">Edulink</h1>
            </div>

            <h2 style="margin-top:24px;">Reset your password</h2>
            <p class="muted">We received a request to reset your password. Click the button below to choose a new one.</p>

            <p style="margin:24px 0;">
              <a class="btn primary" href="%s" target="_blank" rel="noopener">Reset Password</a>
            </p>

            <p class="muted">If the button doesn’t work, copy and paste this link into your browser:</p>
            <p><a class="link" href="%s" target="_blank" rel="noopener">%s</a></p>

            <p class="footer">If you didn’t request this, you can safely ignore this email.</p>
          </div>
        </body>
        </html>
        """.formatted(resetLink, resetLink, resetLink);
    }
    public static String joinApprovalHtml(String verifyLink, StudentInfo studentInfo) {
        String studentName = safe(studentInfo != null ? studentInfo.getFullName() : null);
        String studentEmail = safe(studentInfo != null ? studentInfo.getEmail() : null);

        return """
    <!doctype html>
    <html lang="en">
    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <title>Student Join Request</title>
      <style>
        .container{max-width:600px;margin:0 auto;padding:24px;background:#ffffff;border:1px solid #eaeaea;border-radius:12px;font-family:Arial,Helvetica,sans-serif;color:#111}
        .brand{display:flex;align-items:center;gap:10px}
        .logo{width:36px;height:36px;border-radius:8px;object-fit:cover}
        .title{font-size:20px;margin:0}
        .muted{color:#666}
        .btn{display:inline-block;padding:12px 18px;border-radius:8px;text-decoration:none}
        .primary{background:#0a7c2a;color:#fff}
        .card{border:1px solid #eee;border-radius:10px;padding:14px;background:#fafafa;margin:18px 0}
        .row{margin:6px 0}
        .label{display:inline-block;min-width:110px;color:#666}
        .link{word-break:break-all;color:#0a66c2;text-decoration:none}
        .footer{font-size:12px;color:#888;margin-top:24px}
      </style>
    </head>
    <body style="background:#f5f6f8;padding:24px;">
      <div class="container">
        <div class="brand">
          <img class="logo" src="https://media.licdn.com/dms/image/v2/D4D0BAQG5bEM7JQyMUg/company-logo_100_100/B4DZi4rlSrH0AQ-/0/1755445091818?e=1759363200&v=beta&t=QJ8gD6_YFy7fhbys4MQtXZJQF8_k6VXpgfRZMgW4OUM" alt="Edulink Logo" />
          <h1 class="title">Edulink</h1>
        </div>

        <h2 style="margin-top:24px;">Classroom approval request</h2>
        <p class="muted">A student has requested to join your classroom. Review the details and approve if appropriate.</p>

        <div class="card">
          <div class="row"><span class="label">Student Name:</span> <strong>%s</strong></div>
          <div class="row"><span class="label">Student Email:</span> <strong>%s</strong></div>
        </div>

        <p style="margin:24px 0;">
          <a href="%s"
             target="_blank"
             rel="noopener"
             style="
               display:inline-block;
               padding:12px 24px;
               background-color:#007BFF;
               color:#ffffff;
               text-decoration:none;
               border-radius:6px;
               font-weight:bold;
             ">
             Approve Student
          </a>
        </p>

        <p class="muted">Or open this link:</p>
        <p><a class="link" href="%s" target="_blank" rel="noopener">%s</a></p>

        <p class="footer">If you did not expect this, no action is required.</p>
      </div>
    </body>
    </html>
   """.formatted(studentName, studentEmail, verifyLink, verifyLink, verifyLink);
    }

    private static String safe(String v){
        return v == null ? "" : v.replace("<","&lt;").replace(">","&gt;");
    }
}
