import 'package:flutter/material.dart';
import 'package:kickoff_frontend/components/input_container.dart';
import 'package:kickoff_frontend/constants.dart';

class RoundedConfirmPasswordInput extends StatelessWidget {
  const RoundedConfirmPasswordInput({
    Key? key,
    required this.hint
  }) : super(key: key);

  final String hint;
  static TextEditingController ConfirmPassword =TextEditingController();
  @override
  Widget build(BuildContext context) {
    return InputContainer(
        child: TextField(
          cursorColor: kPrimaryColor,
          controller: ConfirmPassword,
          obscureText: true,
          decoration: InputDecoration(
              icon: Icon(Icons.lock, color: kPrimaryColor),
              hintText: hint,
              border: InputBorder.none
          ),
        ));
  }
}