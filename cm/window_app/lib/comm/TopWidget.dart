import 'package:flutter/material.dart';

Widget getTopMenu(){
  return Row(
    children: [
      Container(
          width:110,
          height:45,
          decoration: BoxDecoration(
              border: Border.all(color: Colors.grey),
              borderRadius: BorderRadius.circular(50)
          ),
          child: const Padding(
              padding: EdgeInsets.fromLTRB(10,0,10,0),
              child:
              Align (
                alignment: Alignment.center,
                child:TextField(
                  decoration: InputDecoration(
                      suffix: Icon(Icons.search),
                      border: InputBorder.none
                  ),
                ),

              )
          )
      )

    ],
  );
}