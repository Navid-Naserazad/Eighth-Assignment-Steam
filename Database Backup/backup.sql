PGDMP                          {           steam    15.3    15.3 
    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                        0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    24671    steam    DATABASE     �   CREATE DATABASE steam WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1256';
    DROP DATABASE steam;
                postgres    false            �            1259    24713    accounts    TABLE     l   CREATE TABLE public.accounts (
    id text,
    username text,
    password text,
    date_of_birth text
);
    DROP TABLE public.accounts;
       public         heap    postgres    false            �            1259    24718 	   downloads    TABLE     e   CREATE TABLE public.downloads (
    account_id text,
    game_id text,
    download_count integer
);
    DROP TABLE public.downloads;
       public         heap    postgres    false            �            1259    24672    games    TABLE     �   CREATE TABLE public.games (
    id text,
    title text,
    developer text,
    genre text,
    price double precision,
    release_year integer,
    controller_support boolean,
    reviews integer,
    size integer,
    file_path text
);
    DROP TABLE public.games;
       public         heap    postgres    false            �          0    24713    accounts 
   TABLE DATA           I   COPY public.accounts (id, username, password, date_of_birth) FROM stdin;
    public          postgres    false    215   �	       �          0    24718 	   downloads 
   TABLE DATA           H   COPY public.downloads (account_id, game_id, download_count) FROM stdin;
    public          postgres    false    216   
       �          0    24672    games 
   TABLE DATA              COPY public.games (id, title, developer, genre, price, release_year, controller_support, reviews, size, file_path) FROM stdin;
    public          postgres    false    214   p
       �   ~   x�KN22LJ�H�51K��i�I�i�&�&�)��i��y�e�)�*F�*�*�Fe.���z�%�Ρ�aUe���z�9�e!�����a�&�ne�~���ay��zΜ��F���\1z\\\ T�"C      �   B   x��ʱ�@�z��b����K�L6Z�i=�|>#]kįr���-�R2Ư��J����/4      �   R  x���Mo�0���W�mO�0���&i{�BەV��0%n #ۤ���k �n���	��gƯ�Q{�!ce{Bn����R��H�b�Wʒ|�e`!	!�ج8�o�8$ݠ����;��U�Pk�[Y��~�qm�[�F�7B�����p�!}$͹lDE�I6ɮ��Z0�8��^i��y��V��-�U��d	���R�pדֲ�\Å�(�&��,s��� M cѼL�sI�RvT�D�n���Tq0Vh�����Y�R'�<H6�|Ԟ�Ye���ҽ�GY����`-�B5���(��F�BZi.�f8������ح�v\�ɶ�[��\��wᚹS5-�Z��;���� !�y�n��^��џ����_�`�&�4�I=�&j�{��{¯�{���i]�}�ZXo�A�7:�!؟[�_���lf�hف0]���v}{ ��EZ4�/�2�[-,U�c
�n.�$��'�d��ȅ�Q5��E[�����{�E�c.����F{z��%��s����ԇ(�w�M��YzNư�4�Vum�y���e�yG�t9����{2���������`�8��g3U1į��O TX     